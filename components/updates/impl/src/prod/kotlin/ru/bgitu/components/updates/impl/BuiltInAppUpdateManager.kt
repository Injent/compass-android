package ru.bgitu.components.updates.impl

import android.content.Context
import android.content.Intent
import android.os.StatFs
import androidx.core.content.FileProvider
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
import ru.bgitu.components.updates.api.model.NativeUpdateInfo
import ru.bgitu.components.updates.api.model.UnknownUpdateInfo
import ru.bgitu.components.updates.api.model.UpdateAvailability
import ru.bgitu.components.updates.api.model.UpdateInfo
import ru.bgitu.components.updates.impl.internal.AppUpdateSaver
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.di.AppDispatchers
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

class BuiltInAppUpdateManager(
    private val context: Context,
    private val compassService: CompassService,
    private val settings: SettingsRepository,
    private val appDispatchers: AppDispatchers
) : AppUpdateManager {
    private val updateSaver by lazy { AppUpdateSaver(context) }
    private var listener: InstallStateListener? = null
    private val managerScope = CoroutineScope(appDispatchers.ioDispatcher + SupervisorJob())

    override val appUpdateInfo = flow<UpdateInfo> {
        val updateResponse = compassService.getUpdateAvailability().getOrThrow()

        if (updateResponse.versionCode > context.versionCode) {
            settings.updateMetadata {
                it.copy(
                    newestUpdateChecksum = updateResponse.checksum,
                    availableVersionCode = updateResponse.versionCode
                )
            }
            emit(updateResponse.toExternalModel())
        }
    }
        .onStart { emit(UnknownUpdateInfo) }
        .catch {
            it.printStackTrace()
            emit(UnknownUpdateInfo)
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

        this@BuiltInAppUpdateManager.listener = InstallStateListener {
            trySend(it)
        }
        awaitClose {
            this@BuiltInAppUpdateManager.listener = null
        }
    }
        .onStart {
            emit(InstallState.NothingToInstall)
        }

    override fun startUpdateFlow(updateInfo: UpdateInfo) {
        managerScope.launch {
            runResulting {
                if (updateInfo !is NativeUpdateInfo) {
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
                            this@BuiltInAppUpdateManager.listener?.onStateChange(it)
                        }
                        .collect()
                }
            }
                .onFailure {
                    this@BuiltInAppUpdateManager.listener?.onStateChange(InstallState.Failed(it.details))
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
                println(checksum)
                println(availableVersionCode)
                println("${it.exists()} ${isUpdateValid(availableVersionCode, checksum)}")
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
        try {
            val availableVersionCode = runBlocking {
                settings.metadata.first().availableVersionCode
            }
            val currentVersionCode = context.versionCode
            context.updatesDir.listFiles { _, name ->
                if (currentVersionCode == availableVersionCode) {
                    return@listFiles true
                }
                (name ?: return@listFiles false)
                    .endsWith(availableVersionCode.toString()).not()
            }
                ?.forEach(File::delete)

            managerScope.launch {
                val updateIsValid = isUpdateValid(
                    availableVersionCode,
                    settings.metadata.first().newestUpdateChecksum
                )

                if (!updateIsValid && availableVersionCode <= context.versionCode) {
                    context.updateApkFile(availableVersionCode).delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun isUpdateValid(versionCode: Long, checksum: String): Boolean {
        val file = context.updateApkFile(versionCode)
        if (file.exists().not()) return false
        return withContext(appDispatchers.ioDispatcher) {
            val digest = MessageDigest.getInstance("SHA-256")
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            val input = file.inputStream()
            var read = input.read(buffer, 0, DEFAULT_BUFFER_SIZE)
            while (read > -1) {
                digest.update(buffer, 0, read)
                read = input.read(buffer, 0, DEFAULT_BUFFER_SIZE)
            }

            input.close()
            val a = digest.digest().toHexString()
            println("$a = $checksum")
            a == checksum
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

private fun UpdateAvailabilityResponse.toExternalModel(): NativeUpdateInfo {
    return NativeUpdateInfo(
        availableVersionCode = versionCode,
        totalBytesToDownload = size,
        forced = true,
        downloadUrl = downloadUrl,
        updateAvailability = UpdateAvailability.UPDATE_AVAILABLE,
        checksum = checksum
    )
}