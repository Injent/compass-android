package ru.bgitu.components.sync.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import ru.bgitu.components.sync.util.successOrRetryUntil
import ru.bgitu.components.sync.util.toForegroundInfo
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.data.util.Synchronizer
import ru.bgitu.core.datastore.DataVersions
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.notifications.Notifier

class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val settings: SettingsRepository,
    private val scheduleRepository: ScheduleRepository,
    private val notifier: Notifier,
) : CoroutineWorker(appContext, params), Synchronizer, KoinComponent {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return notifier.backgroundWorkNotification().toForegroundInfo()
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        if (settings.credentials.first() == null) return@withContext Result.failure()

        val syncedSuccessfully = awaitAll(
            async { scheduleRepository.sync() }
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
        fun start() = OneTimeWorkRequestBuilder<SyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncWorkerConstraints)
            .build()
    }
}