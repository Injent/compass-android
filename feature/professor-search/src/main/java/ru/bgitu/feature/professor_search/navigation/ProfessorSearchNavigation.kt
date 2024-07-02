package ru.bgitu.feature.professor_search.navigation

import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.getId
import ru.bgitu.core.navigation.parent
import ru.bgitu.feature.professor_search.presentation.ProfessorNavHost
import ru.bgitu.feature.professor_search.presentation.ProfessorScheduleScreen
import ru.bgitu.feature.professor_search.presentation.details.ProfessorDetailsScreen
import ru.bgitu.feature.professor_search.presentation.details.ProfessorDetailsViewModel

fun NavGraphBuilder.professorSearchScreen() {
    composable<Screen.ProfessorSearch>(
        popEnterTransition = { slideInHorizontally { -it } },
        exitTransition = {
            if (this.targetState.destination.id == getId<Screen.ProfessorSearch>()) {
                slideOutHorizontally { -it }
            } else {
                fadeOut()
            }
        }
    ) {
        val route = it.toRoute<Screen.ProfessorSearch>()

        ProfessorNavHost(
            professorName = route.professorName,
            backStackEntry = it.parent(LocalNavController.current)
        )
    }
}

internal fun NavGraphBuilder.professorScheduleScreen(
    onBack: () -> Unit,
    backStackEntry: NavBackStackEntry
) {
    composable<ProfessorScheduleScreen> {
        val route = it.toRoute<ProfessorScheduleScreen>()

        val viewModel: ProfessorDetailsViewModel = koinViewModel(
            key = route.professorName,
            viewModelStoreOwner = backStackEntry
        ) {
            parametersOf(route.professorName)
        }

        ProfessorDetailsScreen(
            viewModel = viewModel,
            onBack = onBack
        )
    }
}