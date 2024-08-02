package ru.bgitu.components.sync.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.components.sync.WorkManagerSyncManager
import ru.bgitu.components.sync.workers.RefreshTokenWorker
import ru.bgitu.components.sync.workers.SyncWorker
import ru.bgitu.core.common.di.CommonQualifiers
import ru.bgitu.core.data.util.SyncManager

val SyncModule = module {
    worker {
        SyncWorker(
            appContext = androidContext(),
            params = get(),
            settings = get(),
            scheduleRepository = get(),
        )
    } bind SyncWorker::class

    worker {
        RefreshTokenWorker(
            appContext = androidContext(),
            params = get(),
            settingsRepository = get(),
            notifier = get(),
            ioDispatcher = get(CommonQualifiers.DispatcherIO)
        )
    } bind RefreshTokenWorker::class

    single {
        WorkManagerSyncManager(
            context = androidContext(),
            settingsRepository = get()
        )
    } bind SyncManager::class
}