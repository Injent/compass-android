package ru.bgitu.core.data.util

import ru.bgitu.core.datastore.DataVersions

interface Synchronizer {
    suspend fun dataVersions(): DataVersions
    suspend fun updateChangeListVersions(update: DataVersions.() -> DataVersions)
    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
}

interface Syncable {
    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}