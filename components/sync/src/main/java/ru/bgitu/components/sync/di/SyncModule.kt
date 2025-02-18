package ru.bgitu.components.sync.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.components.sync.WorkManagerSyncManager
import ru.bgitu.core.data.util.SyncManager

val SyncModule = module {
    singleOf(::WorkManagerSyncManager) bind SyncManager::class
}