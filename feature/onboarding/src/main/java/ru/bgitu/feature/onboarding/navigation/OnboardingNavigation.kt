package ru.bgitu.feature.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.onboarding.presentation.OnboardingRoute

fun NavGraphBuilder.onboardingRoute() {
    composable<Screen.Onboarding> {
        OnboardingRoute()
    }
}