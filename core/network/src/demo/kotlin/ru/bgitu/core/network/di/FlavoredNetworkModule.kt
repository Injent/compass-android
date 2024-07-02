package ru.bgitu.core.network.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.demo.DemoCompassService

internal val FlavoredNetworkModule = module {
    single {
        DemoCompassService()
    } bind CompassService::class
}