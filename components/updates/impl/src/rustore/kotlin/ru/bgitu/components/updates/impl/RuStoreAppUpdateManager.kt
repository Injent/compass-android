package ru.bgitu.components.updates.impl

import android.content.Context
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.model.InstallState
import ru.bgitu.components.updates.api.model.UnknownUpdateInfo
import ru.bgitu.components.updates.api.model.UpdateInfo
import ru.bgitu.components.updates.impl.model.RuStoreUpdateInfo
import ru.bgitu.core.common.TextResource
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallStatus
import kotlin.coroutines.suspendCoroutine

private typealias InsState = ru.rustore.sdk.appupdate.model.InstallState

class RuStoreAppUpdateManager(
    context: Context
) : AppUpdateManager {
    private val updateManager = RuStoreAppUpdateManagerFactory.create(context)

    override val appUpdateInfo: Flow<UpdateInfo> = callbackFlow {
        val updateInfoTask = updateManager.getAppUpdateInfo()

        updateInfoTask
            .addOnSuccessListener {
                trySend(RuStoreUpdateInfo(it))
            }
            .addOnFailureListener {
                trySend(UnknownUpdateInfo)
            }

        awaitClose {
            updateInfoTask.cancel()
        }
    }
        .catch { e ->
            e.printStackTrace()
        }
        .onStart {
            emit(UnknownUpdateInfo)
        }

    override val installState: Flow<InstallState> = callbackFlow {
        val installListener = InstallStateUpdateListener {
            trySend(it.toExternalModel())
        }
        updateManager.registerListener(installListener)

        awaitClose {
            updateManager.unregisterListener(installListener)
        }
    }
        .onStart {
            emit(InstallState.Unknown)
        }

    override fun startUpdateFlow(updateInfo: UpdateInfo) {
        val appUpdateInfo = (updateInfo as? RuStoreUpdateInfo)?.data ?: return

        updateManager.startUpdateFlow(
            appUpdateInfo = appUpdateInfo,
            appUpdateOptions = AppUpdateOptions.Builder().appUpdateType(AppUpdateType.IMMEDIATE).build()
        )
    }

    override suspend fun completeUpdate(): ru.bgitu.core.common.Result<Unit> =
        suspendCoroutine { continuation ->
            updateManager.completeUpdate(
                appUpdateOptions = AppUpdateOptions.Builder().appUpdateType(AppUpdateType.FLEXIBLE)
                    .build()
            )
                .addOnCompletionListener { e ->
                    Result.success(
                        if (e == null) {
                            ru.bgitu.core.common.Result.Success(Unit)
                        } else ru.bgitu.core.common.Result.Failure(e)
                    ).also { continuation.resumeWith(it) }
                }
        }

    override fun safelyClearUpdates() {}
}

private fun InsState.toExternalModel(): InstallState {
    return when (this.installStatus) {
        InstallStatus.DOWNLOADED -> InstallState.Downloaded
        InstallStatus.DOWNLOADING -> InstallState.Downloading(totalBytesToDownload, bytesDownloaded)
        InstallStatus.FAILED -> InstallState.Failed(TextResource.Plain("Unexpected error"))
        else -> InstallState.NothingToInstall
    }
}