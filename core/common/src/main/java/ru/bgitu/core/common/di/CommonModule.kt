package ru.bgitu.core.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.dsl.module

class AppDispatchers(
    val ioDispatcher: CoroutineDispatcher,
    val defaultDispatcher: CoroutineDispatcher,
    val mainDispatcher: CoroutineDispatcher
)

val CommonModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            encodeDefaults = true
        }
    }

    single {
        AppDispatchers(
            ioDispatcher = Dispatchers.IO,
            defaultDispatcher = Dispatchers.Default,
            mainDispatcher = Dispatchers.Main
        )
    }
}