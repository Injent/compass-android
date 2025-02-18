package ru.bgitu.core.data.util

import kotlinx.coroutines.flow.Flow

interface SyncManager {
    val syncState: Flow<SyncStatus>

    fun requestSync(isManualSync: Boolean = false)

    companion object {
        const val ACTION_SYNC_COMPLETED = "ru.bgitu.app.SYNC_COMPLETED"
        const val EXTRA_RESULT = "result"
        const val EXTRA_IS_MANUAL = "isManual"
        const val SYNC_COMPLETED = 0
        const val SYNC_FAIL = 1
    }
}