package ru.bgitu.app.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import ru.bgitu.core.common.CommonDrawables
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.about.navigation.aboutRoute
import ru.bgitu.feature.findmate.navigation.findMateGraph
import ru.bgitu.feature.groups.navigation.groupsRoute
import ru.bgitu.feature.help.navigation.helpRoute
import ru.bgitu.feature.home.impl.navigation.homeRoute
import ru.bgitu.feature.input.navigation.inputRoute
import ru.bgitu.feature.login.navigation.loginRoute
import ru.bgitu.feature.notes.navigation.notesRoute
import ru.bgitu.feature.onboarding.navigation.onboardingRoute
import ru.bgitu.feature.professor_search.navigation.professorSearchRoute
import ru.bgitu.feature.profile.navigation.profileRoute
import ru.bgitu.feature.profile_settings.navigation.profileSettingsRoutes
import ru.bgitu.feature.settings.navigation.settingsRoute

@Composable
fun AppNavigator(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Loading,
        modifier = modifier
    ) {
        composable<Screen.Loading> { LoadingScreen() }
        onboardingRoute()
        inputRoute()
        aboutRoute()
        helpRoute()
        settingsRoute()
        profileSettingsRoutes()
        groupsRoute()
        loginRoute()

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
        findMateGraph()
        notesRoute()
        profileRoute()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(CommonDrawables.app_logo),
            contentDescription = null,
            modifier = Modifier.size(118.dp)
        )
    }
}