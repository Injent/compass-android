package ru.bgitu.app.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.about.navigation.aboutRoute
import ru.bgitu.feature.groups.navigation.groupsRoute
import ru.bgitu.feature.help.navigation.helpRoute
import ru.bgitu.feature.home.impl.navigation.homeRoute
import ru.bgitu.feature.input.navigation.inputRoute
import ru.bgitu.feature.professor_search.navigation.professorSearchRoute
import ru.bgitu.feature.profile.navigation.profileRoute
import ru.bgitu.feature.settings.navigation.settingsRoute

@Composable
fun AppNavigator(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.MainGraph,
        modifier = modifier
    ) {
        inputRoute()
        aboutRoute()
        helpRoute()
        settingsRoute()
        groupsRoute()

        mainGraph()
    }
}

private fun NavGraphBuilder.mainGraph() {
    navigation<Screen.MainGraph>(
        startDestination = Screen.Home,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        homeRoute()
        professorSearchRoute()
        profileRoute()
    }
}