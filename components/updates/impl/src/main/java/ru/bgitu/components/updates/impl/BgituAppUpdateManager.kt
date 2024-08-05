package ru.bgitu.components.updates.impl

import android.content.Context
import android.content.Intent
import android.os.StatFs
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.InstallStateListener
import ru.bgitu.components.updates.api.model.InstallState
import ru.bgitu.components.updates.api.model.UpdateAvailability
import ru.bgitu.components.updates.api.model.UpdateInfo
import ru.bgitu.components.updates.impl.internal.AppUpdateSaver
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.exception.DetailedException
import ru.bgitu.core.common.getOrThrow
import ru.bgitu.core.common.runResulting
import ru.bgitu.core.common.versionCode
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.model.response.UpdateAvailabilityResponse
import java.io.File
import java.security.MessageDigest


internal val Context.updatesDir: File
    get() = safeCacheDir.resolve("updates").apply { mkdirs() }

internal val Context.safeCacheDir: File
    get() {
        val cacheDir = checkNotNull(cacheDir) { "cacheDir is null" }
        return cacheDir.apply { mkdirs() }
    }

internal fun Context.updateApkFile(versionCode: Long): File {
    return File(updatesDir, "update_$versionCode")
}

class BgituAppUpdateManager(
    private val context: Context,
    private val compassService: CompassService,
    private val settings: SettingsRepository,
    private val ioDispatcher: CoroutineDispatcher
) : AppUpdateManager {
    private val updateSaver by lazy { AppUpdateSaver(context) }
    private val listeners = mutableSetOf<InstallStateListener>()
    private val managerScope = CoroutineScope(ioDispatcher + SupervisorJob())

    override val appUpdateInfo = flow<UpdateInfo> {
        val currentVersionCode = context.versionCode
        val updateResponse = compassService.getUpdateAvailability().getOrThrow()

        if (updateResponse.versionCode > currentVersionCode) {
            settings.updateMetadata {
                it.copy(
                    newestUpdateChecksum = updateResponse.checksum,
                    availableVersionCode = updateResponse.versionCode
                )
            }
            emit(updateResponse.toExtrenalModel(currentVersionCode = currentVersionCode))
        }
    }
        .onStart { emit(UpdateInfo.Unknown) }
        .catch {
            it.printStackTrace()
            emit(UpdateInfo.Unknown)
        }

    override val installState = callbackFlow {
        val (availableVersionCode, updateChecksum) = with(settings.metadata.first()) {
            availableVersionCode to newestUpdateChecksum
        }

        val updateIsValid = isUpdateValid(
            versionCode = availableVersionCode,
            checksum = updateChecksum
        )
        if (context.updateApkFile(availableVersionCode).exists() && updateIsValid) {
            trySend(InstallState.Downloaded)
        }

        val listener = InstallStateListener {
            trySend(it)
        }
        registerListener(listener)
        awaitClose {
            unregisterListener(listener)
        }
    }
        .onStart {
            emit(InstallState.NothingToInstall)
        }

    override fun startUpdateFlow(updateInfo: UpdateInfo) {
        managerScope.launch {
            runResulting {
                if (updateInfo !is UpdateInfo.NativeUpdate) {
                    throw DetailedException(TextResource.Id(R.string.error_no_updates))
                }

                val currentAppVersion = context.versionCode
                val updateFile = context.updateApkFile(updateInfo.availableVersionCode)
                if (updateFile.exists()
                    && !isUpdateValid(updateInfo.availableVersionCode, updateInfo.checksum)) {
                    safelyClearUpdates()
                    throw DetailedException(TextResource.Id(R.string.error_invalid))
                }

                if (getAvailableSizeOf(context.updatesDir) <= updateInfo.totalBytesToDownload) {
                    throw DetailedException(TextResource.Id(R.string.error_storage))
                }

                if (currentAppVersion < updateInfo.availableVersionCode) {
                    updateSaver.downloadAndSave(
                        versionCode = updateInfo.availableVersionCode,
                        url = updateInfo.downloadUrl,
                        destination = context.updateApkFile(updateInfo.availableVersionCode)
                    )
                        .transformWhile {
                            emit(it)
                            it !is InstallState.Downloaded && it !is InstallState.Failed
                        }
                        .onEach {
                            for (listener in listeners) {
                                listener.onStateChange(it)
                            }
                        }
                        .collect()
                }
            }
                .onFailure {
                    for (listener in listeners) {
                        listener.onStateChange(InstallState.Failed(it.details))
                    }
                }
        }
    }

    override suspend fun completeUpdate(): Result<Unit> {
        // Finding newest downloaded update
        return runResulting {
            val (availableVersionCode, checksum) = with(settings.metadata.first()) {
                availableVersionCode to newestUpdateChecksum
            }
            val updateApk = context.updateApkFile(availableVersionCode).takeIf {
                it.exists() && isUpdateValid(availableVersionCode, checksum)
            }
                ?: run {
                    safelyClearUpdates()
                    throw DetailedException(TextResource.Id(R.string.error_invalid))
                }

            val success = installPackage(
                context = context,
                apkFile = updateApk,
            )
            if (!success) {
                safelyClearUpdates()
                throw DetailedException(TextResource.Id(R.string.error_invalid))
            }
        }
    }

    override fun safelyClearUpdates() {
        runResulting {
            val availableVersionCode = runBlocking {
                settings.metadata.first().availableVersionCode
            }
            context.updatesDir.listFiles { _, name ->
                name?.endsWith(availableVersionCode.toString())?.not() ?: true
            }
                ?.forEach { file ->
                    file.delete()
                }

            managerScope.launch {
                val updateIsValid = isUpdateValid(
                    availableVersionCode,
                    settings.metadata.first().newestUpdateChecksum
                )

                if (!updateIsValid && availableVersionCode <= context.versionCode) {
                    context.updateApkFile(availableVersionCode).delete()
                }
            }
        }
    }

    /**
     * return false if apk file with update is invalid or outdated
     */
    private fun installPackage(
        context: Context,
        apkFile: File,
    ): Boolean {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            apkFile
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, APK_MIME)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)

        return true
    }

    override fun registerListener(listener: InstallStateListener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: InstallStateListener) {
        listeners.remove(listener)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun isUpdateValid(versionCode: Long, checksum: String): Boolean {
        val file = context.updateApkFile(versionCode)
        if (file.exists().not()) return false
        return withContext(ioDispatcher) {
            val digest = MessageDigest.getInstance("SHA-256")
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            val input = file.inputStream()
            var read = input.read(buffer, 0, DEFAULT_BUFFER_SIZE)
            while (read > -1) {
                digest.update(buffer, 0, read)
                read = input.read(buffer, 0, DEFAULT_BUFFER_SIZE)
            }

            input.close()
            digest.digest().toHexString() == checksum
        }
    }

    private fun getAvailableSizeOf(directory: File): Long {
        val stat = StatFs(directory.path)
        return stat.blockSizeLong * stat.blockCountLong
    }

    companion object {
        private const val APK_MIME = "application/vnd.android.package-archive"
    }
}

private fun UpdateAvailabilityResponse.toExtrenalModel(
    currentVersionCode: Long
): UpdateInfo.NativeUpdate {
    return UpdateInfo.NativeUpdate(
        availableVersionCode = versionCode,
        totalBytesToDownload = size,
        forced = forceUpdateVersions.max() > currentVersionCode,
        downloadUrl = downloadUrl,
        updateAvailability = UpdateAvailability.UPDATE_AVAILABLE,
        checksum = checksum
    )
}