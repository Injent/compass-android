package ru.bgitu.feature.notes.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.back
import ru.bgitu.feature.notes.presentation.details.NoteDetailsRoute
import ru.bgitu.feature.notes.presentation.pane.NotesListDetailRoute

internal const val SUBJECT_NAME = "subjectName"

fun NavGraphBuilder.notesRoute() {
    composable<Screen.Notes> {
        NotesListDetailRoute()
    }
    composable<Screen.NoteDetails> {
        val navController = LocalNavController.current

        NoteDetailsRoute(
            onBack = navController::back
        )
    }
}