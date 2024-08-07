package ru.bgitu.feature.login.navigation

import androidx.compose.animation.fadeIn
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.parent
import ru.bgitu.feature.login.presentation.login.LoginRoute

fun NavGraphBuilder.loginRoute() {
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
}