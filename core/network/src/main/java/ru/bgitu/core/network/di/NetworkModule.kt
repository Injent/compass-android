package ru.bgitu.core.network.di

import org.koin.dsl.module

val NetworkModule = module {
    includes(FlavoredNetworkModule)
}