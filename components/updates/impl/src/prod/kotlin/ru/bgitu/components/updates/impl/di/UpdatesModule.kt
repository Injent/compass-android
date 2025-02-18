package ru.bgitu.components.updates.impl.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.StoreSync
import ru.bgitu.components.updates.impl.BuiltInAppUpdateManager
import ru.bgitu.components.updates.impl.internal.NoStoreSync
import ru.bgitu.core.common.di.CommonQualifiers

val UpdatesModule = module {
    factory {
        NoStoreSync()
    } bind StoreSync::class

    single {
        BuiltInAppUpdateManager(
            compassService = get(),
            context = androidContext(),
            settings = get(),
            ioDispatcher = get(CommonQualifiers.DispatcherIO)
        )
    } bind AppUpdateManager::class
}