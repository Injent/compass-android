package ru.bgitu.components.updates.impl.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.StoreSync
import ru.bgitu.components.updates.impl.BuiltInAppUpdateManager
import ru.bgitu.components.updates.impl.internal.NoStoreSync
import ru.bgitu.components.updates.impl.work.AppUpdateWorker
import ru.bgitu.core.common.di.CommonQualifiers

val UpdatesModule = module {
    single {
        BuiltInAppUpdateManager(
            compassService = get(),
            context = androidContext(),
            settings = get(),
            ioDispatcher = get(CommonQualifiers.DispatcherIO)
        )
    } bind AppUpdateManager::class

    single {
        NoStoreSync()
    } bind StoreSync::class

    worker {
        AppUpdateWorker(
            appContext = androidContext(),
            params = get(),
            ioDispatcher = get(CommonQualifiers.DispatcherIO),
            notifier = get(),
            fileDownloader = get()
        )
    }
}