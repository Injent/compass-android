package ru.bgitu.feature.home.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.datetime.toLocalDate
import ru.bgitu.core.common.HOME_DEEPLINK
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.home.impl.presentation.HomeScreen

fun NavGraphBuilder.homeRoute() {
    composable<Screen.Home>(
        deepLinks = listOf(
            navDeepLink { uriPattern = "$HOME_DEEPLINK/{date}" }
        )
    ) { backStackEntry ->
        HomeScreen(
            initialScheduleDate = backStackEntry.arguments?.getString("date")?.toLocalDate(),
        )
    }
}