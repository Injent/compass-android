package ru.bgitu.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.settings.presentation.settings.SettingsScreen

fun NavGraphBuilder.settingsRoute() {
    composable<Screen.Settings> {
        SettingsScreen()
    }
}