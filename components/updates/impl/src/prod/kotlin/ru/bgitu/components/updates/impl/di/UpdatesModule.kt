package ru.bgitu.components.updates.impl.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.StoreSync
import ru.bgitu.components.updates.impl.BuiltInAppUpdateManager
import ru.bgitu.components.updates.impl.internal.NoStoreSync

val UpdatesModule = module {
    factory {
        NoStoreSync()
    } bind StoreSync::class

    singleOf(::BuiltInAppUpdateManager) bind AppUpdateManager::class
}