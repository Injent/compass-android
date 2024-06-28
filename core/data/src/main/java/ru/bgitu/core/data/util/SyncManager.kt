package ru.bgitu.core.data.util

import kotlinx.coroutines.flow.Flow
import ru.bgitu.core.data.model.CloudMessagingTokenType

interface SyncManager {
    val syncState: Flow<SyncStatus>

    fun requestSync()

    fun refreshServicesToken(token: String, type: CloudMessagingTokenType)

    fun fullSync()
}