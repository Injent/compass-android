package ru.bgitu.feature.profile.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.profile.presentation.ProfileViewModel

val ProfileModule = module {
    viewModel {
        ProfileViewModel(
            settingsRepository = get(),
        )
    }
}