package ru.bgitu.feature.settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.settings.presentation.settings.SettingsViewModel

val SettingsModule = module {
    viewModel {
        SettingsViewModel(
            settings = get(),
            scheduleNotifier = get()
        )
    }
}