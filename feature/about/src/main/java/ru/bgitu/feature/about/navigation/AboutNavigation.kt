package ru.bgitu.feature.about.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.about.presentation.AboutScreen

fun NavGraphBuilder.about() {
    composable<Screen.About> {
        AboutScreen()
    }
}