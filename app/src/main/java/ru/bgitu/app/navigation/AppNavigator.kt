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
import ru.bgitu.feature.about.navigation.about
import ru.bgitu.feature.findmate.navigation.findMateGraph
import ru.bgitu.feature.groups.navigation.groupsScreen
import ru.bgitu.feature.help.navigation.help
import ru.bgitu.feature.home.impl.navigation.homeScreen
import ru.bgitu.feature.input.navigation.inputScreen
import ru.bgitu.feature.login.navigation.loginRoute
import ru.bgitu.feature.professor_search.navigation.professorSearchScreen
import ru.bgitu.feature.profile.navigation.profileRoute
import ru.bgitu.feature.profile_settings.navigation.profileSettings
import ru.bgitu.feature.settings.navigation.settings

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
        inputScreen()
        about()
        help()
        settings()
        profileSettings()
        groupsScreen()
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
        homeScreen()
        professorSearchScreen()
        findMateGraph()
        profileGraph()
    }
}

private fun NavGraphBuilder.profileGraph() {
    navigation<Screen.ProfileGraph>(
        startDestination = Screen.Profile,
    ) {
        profileRoute()
        profileSettings()
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