package ru.bgitu.core.common.di

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

object CommonQualifiers {

    val DispatcherIO: Qualifier
        get() = qualifier("dispatcherIO")

    val DispatcherDefault: Qualifier
        get() = qualifier("dispatcherIO")
}

val CommonModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            encodeDefaults = true
        }
    }

    factory(
        qualifier = CommonQualifiers.DispatcherDefault
    ) {
        Dispatchers.Default
    }

    factory(
        qualifier = CommonQualifiers.DispatcherIO
    ) {
        Dispatchers.IO
    }
}