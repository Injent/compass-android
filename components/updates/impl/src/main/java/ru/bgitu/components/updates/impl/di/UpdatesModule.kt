package ru.bgitu.components.updates.impl.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.impl.BgituAppUpdateManager
import ru.bgitu.components.updates.impl.work.AppUpdateWorker
import ru.bgitu.core.common.di.CommonQualifiers

val UpdatesModule = module {
    single {
        BgituAppUpdateManager(
            serviceApi = get(),
            context = androidContext(),
            settings = get(),
            ioDispatcher = get(CommonQualifiers.DispatcherIO)
        )
    } bind AppUpdateManager::class

    worker {
        AppUpdateWorker(
            appContext = androidContext(),
            params = get(),
            ioDispatcher = get(CommonQualifiers.DispatcherIO),
            notifier = get(),
        )
    }
}