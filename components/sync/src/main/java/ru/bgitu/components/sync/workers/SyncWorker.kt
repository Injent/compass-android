package ru.bgitu.components.sync.workers

import android.content.Context
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
import ru.bgitu.components.sync.util.SyncManagerConstraints
import ru.bgitu.components.sync.util.successOrRetryUntil
import ru.bgitu.components.sync.util.syncNotification
import ru.bgitu.components.sync.util.toForegroundInfo
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.data.util.Synchronizer
import ru.bgitu.core.datastore.DataVersions
import ru.bgitu.core.datastore.SettingsRepository

class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val settings: SettingsRepository,
    private val scheduleRepository: ScheduleRepository,
) : CoroutineWorker(appContext, params), Synchronizer {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return applicationContext.syncNotification().toForegroundInfo()
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        if (settings.data.first().primaryGroup == null) return@withContext Result.failure()

        val isManualSync = this@SyncWorker.inputData.getBoolean(PARAM_MANUAL_SYNC, false)

        val syncedSuccessfully = awaitAll(
            async { scheduleRepository.sync(isManualSync) }
        ).all { it }

        successOrRetryUntil(syncedSuccessfully)
    }

    override suspend fun dataVersions(): DataVersions {
        return settings.getDataVersions()
    }

    override suspend fun updateChangeListVersions(update: DataVersions.() -> DataVersions) {
        settings.updateDataVersions(update)
    }

    companion object {
        const val PARAM_MANUAL_SYNC = "first_sync"

        fun start(firstSync: Boolean = false) = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInputData(
                workDataOf(PARAM_MANUAL_SYNC to firstSync)
            )
            .setConstraints(SyncManagerConstraints)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
    }
}