package ru.bgitu.feature.login.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.parent
import ru.bgitu.feature.login.presentation.login.LoginRoute
import ru.bgitu.feature.login.presentation.pickgroup.PickGroupRoute
import ru.bgitu.feature.login.presentation.recovery.RecoveryScreen

fun NavGraphBuilder.loginGraph() {
    navigation<Screen.LoginGraph>(
        startDestination = Screen.Login()
    ) {
        composable<Screen.Login>(
            enterTransition = { fadeIn() }
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.Login>()

            LoginRoute(
                viewModel = koinViewModel(
                    viewModelStoreOwner = backStackEntry.parent(LocalNavController.current)
                ) { parametersOf(route.compactScreen) }
            )
        }

        composable<Screen.PickGroup>(
            enterTransition = {
                slideInHorizontally { it / 2 }
            },
            popExitTransition = {
                fadeOut()
            }
        ) {
            val route = it.toRoute<Screen.PickGroup>()

            PickGroupRoute(
                predictedSearhQuery = route.predictedSearchQuery
            )
        }

        composable<Screen.Recovery> {
            RecoveryScreen()
        }
    }
}