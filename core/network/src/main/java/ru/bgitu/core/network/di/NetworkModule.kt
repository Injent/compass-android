package ru.bgitu.core.network.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.common.di.CommonQualifiers
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.ktor.KtorDataSource

val NetworkModule = module {

    single {
        KtorDataSource(
            settingsRepository = get(),
            json = get(),
            cacheDir = get(CommonQualifiers.CacheDir),
            ioDispatcher = get(CommonQualifiers.DispatcherIO)
        )
    } bind CompassService::class
}