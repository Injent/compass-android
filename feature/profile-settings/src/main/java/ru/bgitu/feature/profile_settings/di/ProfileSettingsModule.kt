package ru.bgitu.feature.profile_settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.profile_settings.presentation.ProfileSettingsViewModel

val ProfileSettingsModule = module {
    viewModel {
        ProfileSettingsViewModel(
            settingsRepository = get()
        )
    }
}