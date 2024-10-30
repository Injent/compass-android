package ru.bgitu.feature.notes.presentation.pane

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.StateFlow
import ru.bgitu.core.navigation.Screen

const val NOTE_ID = "noteId"

sealed interface NotesIntent {
    data class SelectNote(val noteId: Int) : NotesIntent
}

class Notes2PaneViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val route = savedStateHandle.toRoute<Screen.Notes>()
    val selectedNoteId: StateFlow<Int?> = savedStateHandle.getStateFlow(NOTE_ID, route.noteId)

    fun onIntent(intent: NotesIntent) {
        when (intent) {
            is NotesIntent.SelectNote -> savedStateHandle[NOTE_ID] = intent.noteId
        }
    }
}