package ru.bgitu.components.sync.workers

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.bgitu.components.sync.util.SyncManagerConstraints
import ru.bgitu.components.sync.util.successOrRetryUntil
import ru.bgitu.components.sync.util.syncNotification
import ru.bgitu.components.sync.util.toForegroundInfo
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.data.util.SyncManager.Companion.ACTION_SYNC_COMPLETED
import ru.bgitu.core.data.util.SyncManager.Companion.EXTRA_IS_MANUAL
import ru.bgitu.core.data.util.SyncManager.Companion.EXTRA_RESULT
import ru.bgitu.core.data.util.SyncManager.Companion.SYNC_COMPLETED
import ru.bgitu.core.data.util.SyncManager.Companion.SYNC_FAIL
import ru.bgitu.core.data.util.Synchronizer
import ru.bgitu.core.datastore.DataVersions
import ru.bgitu.core.datastore.SettingsRepository

class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), Synchronizer, KoinComponent {
    private val settingsRepository by inject<SettingsRepository>()
    private val scheduleRepository by inject<ScheduleRepository>()

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return applicationContext.syncNotification().toForegroundInfo()
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            if (settingsRepository.data.first().primaryGroup == null) return@withContext Result.failure()

            val isManualSync = this@SyncWorker.inputData.getBoolean(PARAM_MANUAL_SYNC, false)

            val syncedSuccessfully = awaitAll(
                async { scheduleRepository.sync(isManualSync) }
            ).all { it }

            // Send broadcast to widget receiver to update it's state
            sendCompleteBroadcast(
                success = syncedSuccessfully,
                manualSync = isManualSync
            )

            successOrRetryUntil(syncedSuccessfully)
        }
    }

    override suspend fun dataVersions(): DataVersions {
        return settingsRepository.getDataVersions()
    }

    override suspend fun updateChangeListVersions(update: DataVersions.() -> DataVersions) {
        settingsRepository.updateDataVersions(update)
    }

    private fun sendCompleteBroadcast(success: Boolean, manualSync: Boolean) {
        applicationContext.sendBroadcast(
            Intent(ACTION_SYNC_COMPLETED).apply {
                setPackage(applicationContext.packageName)
                putExtra(
                    EXTRA_RESULT,
                    if (success) {
                        SYNC_COMPLETED
                    } else SYNC_FAIL
                )
                putExtra(EXTRA_IS_MANUAL, manualSync)
            }
        )
    }

    companion object {
        const val PARAM_MANUAL_SYNC = "manualSync"

        fun start(manualSync: Boolean = false) = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInputData(
                workDataOf(PARAM_MANUAL_SYNC to manualSync)
            )
            .setConstraints(SyncManagerConstraints)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
    }
}