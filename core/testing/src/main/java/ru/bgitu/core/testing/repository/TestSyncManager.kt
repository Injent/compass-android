package ru.bgitu.core.testing.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.bgitu.core.data.model.CloudMessagingTokenType
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.data.util.SyncStatus

class TestSyncManager : SyncManager {
    override val syncState: Flow<SyncStatus> = flowOf(SyncStatus.SUCCEEDED)

    override fun requestSync() {
        // Do nothing
    }

    override fun refreshServicesToken(token: String, type: CloudMessagingTokenType) {
        // Do nothing
    }

    override fun fullSync() {
        // Do nothing
    }
}