package ru.bgitu.feature.profile_settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.profile_settings.presentation.ProfileSettingsRoute

fun NavGraphBuilder.profileSettings() {
    composable<Screen.ProfileSettings> {
        ProfileSettingsRoute()
    }
}