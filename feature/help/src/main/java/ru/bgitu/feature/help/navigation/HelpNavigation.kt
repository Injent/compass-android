package ru.bgitu.feature.help.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.help.presentation.HelpScreen

fun NavGraphBuilder.helpRoute() {
    composable<Screen.Help> {
        HelpScreen()
    }
}