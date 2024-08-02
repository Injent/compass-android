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
import ru.bgitu.feature.profile.navigation.profileRoute
import ru.bgitu.feature.profile_settings.navigation.profileSettings

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
        //inputScreen()
        //about()
        //help()
        //settings()
        //profileSettings()
        //groupsScreen()

        //loginGraph()
        mainGraph()
    }
}

private fun NavGraphBuilder.mainGraph() {
    navigation<Screen.MainGraph>(
        startDestination = Screen.Loading,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        composable<Screen.Loading> {  }
        //homeScreen()
        //professorSearchScreen()
        //findMateGraph()
        //profileGraph()
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