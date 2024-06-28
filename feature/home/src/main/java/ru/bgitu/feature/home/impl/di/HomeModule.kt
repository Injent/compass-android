package ru.bgitu.feature.home.impl.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bgitu.feature.home.impl.presentation.HomeViewModel

val HomeModule = module {
    viewModel { params ->
        HomeViewModel(
            savedStateHandle = params.get(),
            openScheduleDate = params.getOrNull(),
            scheduleRepository = get(),
            context = androidContext(),
            settings = get()
        )
    }
}