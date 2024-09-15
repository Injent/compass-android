package ru.bgitu.core.data.util

import kotlinx.coroutines.flow.Flow
import ru.bgitu.core.data.model.CloudMessagingTokenType

interface SyncManager {
    val syncState: Flow<SyncStatus>

    fun requestSync(isFirstSync: Boolean = false)

    fun refreshServicesToken(token: String, type: CloudMessagingTokenType)

    fun fullSync()
}