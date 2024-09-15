package ru.bgitu.feature.onboarding.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.onboarding.presentation.OnboardingViewModel

val OnboardingModule = module {
    viewModelOf(::OnboardingViewModel)
}