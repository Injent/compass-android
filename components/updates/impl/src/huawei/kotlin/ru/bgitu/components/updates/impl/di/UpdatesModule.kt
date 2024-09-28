package ru.bgitu.components.updates.impl.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.StoreSync
import ru.bgitu.components.updates.impl.HuaweiAppUpdateManager
import ru.bgitu.components.updates.impl.HuaweiStoreSync

val UpdatesModule = module {
    factory {
        HuaweiStoreSync()
    } bind StoreSync::class

    single {
        HuaweiAppUpdateManager(context = androidContext())
    } bind AppUpdateManager::class
}