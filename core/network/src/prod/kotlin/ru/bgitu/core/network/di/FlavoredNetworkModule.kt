package ru.bgitu.core.network.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.ktor.KtorDataSource

internal val FlavoredNetworkModule = module {
    single {
        KtorDataSource(
            client = get(),
            settingsRepository = get()
        )
    } bind CompassService::class
}