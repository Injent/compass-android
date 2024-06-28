package ru.bgitu.components.sync

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import ru.bgitu.components.sync.workers.RefreshTokenWorker
import ru.bgitu.components.sync.workers.SyncWorker
import ru.bgitu.core.data.model.CloudMessagingTokenType
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.data.util.SyncStatus
import ru.bgitu.core.datastore.SettingsRepository

internal const val SyncWorkName = "SyncWorkName"
internal const val ServicesRefreshTokenWorkName = "ServicesRefreshTokenName"

class WorkManagerSyncManager(
    context: Context,
    private val settingsRepository: SettingsRepository,
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

    override fun fullSync() {
        val notAuthed = runBlocking { settingsRepository.credentials.first() == null }
        if (notAuthed) {
            return
        }
        requestSync()
        Firebase.messaging.token.addOnSuccessListener { token ->
            refreshServicesToken(token, CloudMessagingTokenType.GMS)
        }
    }

    override fun requestSync() {
        workManager.enqueueUniqueWork(
            SyncWorkName,
            ExistingWorkPolicy.KEEP,
            SyncWorker.start()
        )
    }

    override fun refreshServicesToken(token: String, type: CloudMessagingTokenType) {
        workManager.enqueueUniquePeriodicWork(
            ServicesRefreshTokenWorkName,
            ExistingPeriodicWorkPolicy.KEEP,
            RefreshTokenWorker.start(token, type)
        )
    }
}