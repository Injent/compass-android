package ru.bgitu.components.updates.api

import kotlinx.coroutines.flow.Flow
import ru.bgitu.components.updates.api.model.InstallState
import ru.bgitu.components.updates.api.model.UpdateInfo
import ru.bgitu.core.common.Result

fun interface InstallStateListener {
    fun onStateChange(installState: InstallState)
}

interface AppUpdateManager {

    val appUpdateInfo: Flow<UpdateInfo>
    val installState: Flow<InstallState>

    fun startUpdateFlow(updateInfo: UpdateInfo)

    suspend fun completeUpdate(): Result<Unit>
    fun registerListener(listener: InstallStateListener)
    fun unregisterListener(listener: InstallStateListener)

    fun safelyClearUpdates()
}