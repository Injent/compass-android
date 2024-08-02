package ru.bgitu.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.profile.presentation.ProfileScreen

fun NavGraphBuilder.profileRoute() {
    composable<Screen.Profile> {
        ProfileScreen()
    }
}