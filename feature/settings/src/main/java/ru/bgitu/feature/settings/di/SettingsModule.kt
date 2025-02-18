package ru.bgitu.feature.settings.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.bgitu.feature.settings.presentation.settings.SettingsViewModel

val SettingsModule = module {
    viewModelOf(::SettingsViewModel)
}