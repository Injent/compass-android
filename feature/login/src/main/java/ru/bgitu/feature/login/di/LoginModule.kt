package ru.bgitu.feature.login.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.login.presentation.login.LoginViewModel
import ru.bgitu.feature.login.presentation.pickgroup.PickGroupViewModel
import ru.bgitu.feature.login.presentation.recovery.RecoveryViewModel

val LoginModule = module {
    viewModel { params ->
        LoginViewModel(
            compassAuthenticator = get(),
            syncManager = get(),
            networkMonitor = get(),
            verificationRequest = params.get()
        )
    }
    viewModel { params ->
        PickGroupViewModel(
            compassAuthenticator = get(),
            syncManager = get(),
            networkMonitor = get(),
            searchQuery = params.getOrNull()
        )
    }
    viewModel {
        RecoveryViewModel()
    }
}