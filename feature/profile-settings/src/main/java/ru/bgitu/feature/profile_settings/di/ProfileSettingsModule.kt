package ru.bgitu.feature.profile_settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.bgitu.feature.profile_settings.data.ProfileRepository
import ru.bgitu.feature.profile_settings.presentation.expert.ExpertApplyViewModel
import ru.bgitu.feature.profile_settings.presentation.settings.ProfileSettingsViewModel
import ru.bgitu.feature.profile_settings.presentation.variants.VariantViewModel

val ProfileSettingsModule = module {
    single {
        ProfileRepository(
            settingsRepository = get(),
            compassService = get()
        )
    }

    viewModelOf(::ProfileSettingsViewModel)
    viewModelOf(::ExpertApplyViewModel)
    viewModel { params ->
        VariantViewModel(
            profileRepository = get(),
            subjectName = params.get()
        )
    }
}