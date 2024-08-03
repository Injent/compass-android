package ru.bgitu.feature.professor_search.presentation

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.bgitu.core.designsystem.components.AppBottomBarTokens
import ru.bgitu.core.navigation.back
import ru.bgitu.core.navigation.push
import ru.bgitu.feature.professor_search.presentation.details.TeacherDetailsScreen
import ru.bgitu.feature.professor_search.presentation.details.TeacherDetailsViewModel
import ru.bgitu.feature.professor_search.presentation.search.TeacherSearchRoute

@Serializable internal object TeacherSearchScreen

@Serializable internal data class TeacherScheduleScreen(val professorName: String)

internal const val KEY_TEACHER_VIEWMODEL = "teacherViewModel"

@SuppressLint("RestrictedApi")
@Composable
fun TeacherRoute() {
    val nestedNavController = rememberNavController()

    val context = LocalContext.current
    val startDestination = remember {
        val activity = context as ComponentActivity

        (activity.viewModelStore[KEY_TEACHER_VIEWMODEL] as? TeacherDetailsViewModel)?.let {
            TeacherScheduleScreen(it.teacherName)
        } ?: TeacherSearchScreen
    }

    NavHost(
        navController = nestedNavController,
        startDestination = startDestination,
        modifier = Modifier.padding(bottom = AppBottomBarTokens.Height)
    ) {
        composable<TeacherSearchScreen> {
            TeacherSearchRoute(
                onNavigateToDetails = { teacherName ->
                    nestedNavController.push(TeacherScheduleScreen(teacherName))
                }
            )
        }
        composable<TeacherScheduleScreen> {
            val route = it.toRoute<TeacherScheduleScreen>()

            TeacherDetailsScreen(
                teacherName = route.professorName,
                onBack = {
                    if (!nestedNavController.back()) {
                        nestedNavController.navigate(TeacherSearchScreen) {
                            popUpTo<TeacherScheduleScreen> { inclusive = true }
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}