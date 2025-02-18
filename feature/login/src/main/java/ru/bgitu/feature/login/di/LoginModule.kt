package ru.bgitu.feature.login.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.login.presentation.login.LoginViewModel

val LoginModule = module {
    viewModel {
        LoginViewModel(
            compassAuthenticator = get(),
            networkMonitor = get(),
        )
    }
}