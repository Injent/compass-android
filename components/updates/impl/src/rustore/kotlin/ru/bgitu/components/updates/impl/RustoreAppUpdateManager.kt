package ru.bgitu.components.updates.impl

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.model.InstallException
import ru.bgitu.components.updates.api.model.InstallState
import ru.bgitu.components.updates.api.model.UpdateAvailability
import ru.bgitu.components.updates.api.model.UpdateInfo
import ru.bgitu.core.common.ResultHandler
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateInfo

private typealias UA = ru.rustore.sdk.appupdate.model.UpdateAvailability
private typealias IS = ru.rustore.sdk.appupdate.model.InstallStatus
private typealias InsState = ru.rustore.sdk.appupdate.model.InstallState

class RustoreAppUpdateManager(
    context: Context
) : AppUpdateManager {
    private val updateManager = RuStoreAppUpdateManagerFactory.create(context)

    override val appUpdateInfo: Flow<UpdateInfo>
        get() = callbackFlow {
            val updateInfoTask = updateManager.getAppUpdateInfo()

            updateInfoTask.addOnSuccessListener {
                trySend(it.toExternalModel())
            }
                .addOnFailureListener {
                    trySend(UpdateInfo.Unknown)
                }

            invokeOnClose {
                updateInfoTask.cancel()
            }
        }

    override val downloadStatus: Flow<InstallState>
        get() = channelFlow {
            val installListener = InstallStateUpdateListener {
                trySend(it.toExternalModel())
            }
            updateManager.registerListener(installListener)

            invokeOnClose {
                updateManager.unregisterListener(installListener)
            }
        }

    override suspend fun startUpdateFlow(): ResultHandler<Unit> {
        updateManager.startUpdateFlow()
    }

    override suspend fun completeUpdate(): ResultHandler<Unit> {
        TODO("Not yet implemented")
    }

    override fun safelyClearUpdates() {
        TODO("Not yet implemented")
    }
}

private fun InsState.toExternalModel(): InstallState {
    return InstallState(
        installStatus = when (installStatus) {
            IS.DOWNLOADED -> InstallStatus.DOWNLOADED
            IS.DOWNLOADING -> InstallStatus.DOWNLOADING
            IS.FAILED -> InstallStatus.FAILED
            IS.INSTALLING -> InstallStatus.INSTALLING
            IS.PENDING -> InstallStatus.PENDING
            else -> InstallStatus.UNKNOWN
        },
        bytesDownloaded = bytesDownloaded,
        totalBytesDownloaded = totalBytesToDownload,
        installErrorCode = when (installErrorCode) {
            4001 -> InstallException.ERROR_UNKNOWN
            4002 -> InstallException.ERROR_DOWNLOAD
            4003 -> InstallException.ERROR_BLOCKED
            4004 -> InstallException.ERROR_INVALID_APK
            4005 -> InstallException.ERROR_CONFLICT
            4006 -> InstallException.ERROR_STORAGE
            4007 -> InstallException.ERROR_INCOMPATIBLE
            4008 -> InstallException.ERROR_APP_NOT_OWNED
            4009 -> InstallException.ERROR_INTERNAL_ERROR
            4010 -> InstallException.ERROR_ABORTED
            4011 -> InstallException.ERROR_APK_NOT_FOUND
            4012 -> InstallException.ERROR_EXTERNAL_SOURCE_DENIED
            9901 -> InstallException.ERROR_ACTIVITY_SEND_INTENT
            else -> InstallException.ERROR_ACTIVITY_UNKNOWN
        }
    )
}

private fun AppUpdateInfo.toExternalModel(): UpdateInfo {
    return UpdateInfo.RuStoreUpdate(
        availableVersionCode = availableVersionCode,
        updateAvailability = when (updateAvailability) {
            UA.UPDATE_NOT_AVAILABLE -> UpdateAvailability.UPDATE_NOT_AVAILABLE
            UA.UPDATE_AVAILABLE -> UpdateAvailability.UPDATE_AVAILABLE
            UA.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            }
            else -> UpdateAvailability.UNKNOWN
        },
        installStatus = when (installStatus) {
            IS.DOWNLOADED -> InstallStatus.DOWNLOADED
            IS.DOWNLOADING -> InstallStatus.DOWNLOADING
            IS.FAILED -> InstallStatus.FAILED
            IS.INSTALLING -> InstallStatus.INSTALLING
            IS.PENDING -> InstallStatus.PENDING
            else -> InstallStatus.UNKNOWN
        }
    )
}