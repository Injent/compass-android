package ru.bgitu.components.updates.impl.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.StoreSync
import ru.bgitu.components.updates.impl.RuStoreAppUpdateManager
import ru.bgitu.components.updates.impl.RuStoreSync

val UpdatesModule = module {
    single {
        RuStoreAppUpdateManager(context = androidContext())
    } bind AppUpdateManager::class

    single {
        RuStoreSync()
    } bind StoreSync::class
}