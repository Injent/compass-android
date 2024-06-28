package ru.bgitu.feature.update.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.update.presentation.AppUpdateViewModel

val AppUpdateModule = module {
    viewModel {
        AppUpdateViewModel(
            appUpdateManager = get(),
            compassRepository = get(),
            updateVersionCode = it.get()
        )
    }
}