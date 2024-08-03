package ru.bgitu.feature.professor_search.navigation

import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.getId
import ru.bgitu.feature.professor_search.presentation.TeacherRoute

fun NavGraphBuilder.professorSearchScreen() {
    composable<Screen.TeacherSearch>(
        popEnterTransition = { slideInHorizontally { -it } },
        exitTransition = {
            if (this.targetState.destination.id == getId<Screen.TeacherSearch>()) {
                slideOutHorizontally { -it }
            } else {
                fadeOut()
            }
        }
    ) {
        TeacherRoute()
    }
}