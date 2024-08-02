package ru.bgitu.components.signin.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.bgitu.components.signin.repository.CompassAuthenticator
import ru.bgitu.components.signin.repository.DefaultCompassAuthenticator

val SignInModule = module {
    single {
        DefaultCompassAuthenticator(
            compassService = get(),
            settingsRepository = get(),
            compassDatabase = get(),
            networkMonitor = get()
        )
    } bind CompassAuthenticator::class
}