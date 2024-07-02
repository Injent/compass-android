package ru.bgitu.feature.professor_search.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.back
import ru.bgitu.core.navigation.replaceAll
import ru.bgitu.feature.professor_search.navigation.professorScheduleScreen
import ru.bgitu.feature.professor_search.presentation.search.ProfessorSearchIntent
import ru.bgitu.feature.professor_search.presentation.search.ProfessorSearchRoute
import ru.bgitu.feature.professor_search.presentation.search.ProfessorSearchViewModel

@Serializable internal object ProfessorSearchScreen

@Serializable internal data class ProfessorScheduleScreen(val professorName: String)

@Composable
fun ProfessorNavHost(
    professorName: String?,
    backStackEntry: NavBackStackEntry
) {
    val viewModel: ProfessorSearchViewModel = koinViewModel(
        viewModelStoreOwner = backStackEntry,
    ) {
        parametersOf(professorName)
    }
    val nestedNavHostStartDestination = remember(professorName, viewModel.professorName) {
        if (professorName != null) {
            ProfessorScheduleScreen(professorName)
        } else {
            ProfessorScheduleScreen(
                professorName = viewModel.professorName ?: return@remember ProfessorSearchScreen
            )
        }
    }
    val nestedNavController = rememberNavController()

    NavHost(
        navController = nestedNavController,
        startDestination = nestedNavHostStartDestination
    ) {
        composable<ProfessorSearchScreen> {
            ProfessorSearchRoute(
                viewModel = viewModel,
                onNavigateToDetails = { professorName ->
                    viewModel.onIntent(ProfessorSearchIntent.SetStartDestination(professorName))
                    nestedNavController.navigate(Screen.ProfessorSchedule(professorName))
                }
            )
        }
        professorScheduleScreen(
            onBack = {
                viewModel.onIntent(ProfessorSearchIntent.SetStartDestination(null))
                if (!nestedNavController.back()) {
                    nestedNavController.replaceAll(Screen.ProfessorSearch())
                }
            },
            backStackEntry = backStackEntry
        )
    }
}