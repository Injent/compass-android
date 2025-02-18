package ru.bgitu.components.sync

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import ru.bgitu.components.sync.workers.SyncWorker
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.data.util.SyncStatus

internal const val SyncWorkName = "SyncWorkName"

class WorkManagerSyncManager(
    context: Context,
) : SyncManager {
    private val workManager = WorkManager.getInstance(context)

    override val syncState: Flow<SyncStatus>
        get() = workManager.getWorkInfosForUniqueWorkLiveData(SyncWorkName)
            .asFlow()
            .map {
                if (it.isEmpty())
                    SyncStatus.IDLE
                else when (it.first().state) {
                    WorkInfo.State.RUNNING -> SyncStatus.RUNNUNG
                    WorkInfo.State.SUCCEEDED -> SyncStatus.SUCCEEDED
                    WorkInfo.State.FAILED, WorkInfo.State.CANCELLED -> SyncStatus.FAILED
                    else -> SyncStatus.IDLE
                }
            }
            .conflate()

    override fun requestSync(isManualSync: Boolean) {
        workManager.enqueueUniqueWork(
            SyncWorkName,
            ExistingWorkPolicy.KEEP,
            SyncWorker.start(isManualSync)
        )
    }
}