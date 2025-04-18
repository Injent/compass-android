package ru.bgitu.feature.notes.presentation.details

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.notes.data.NotesRepository
import ru.bgitu.feature.notes.model.Note

sealed interface NoteDetailsEvent {
    data object Back : NoteDetailsEvent
    data object Delete : NoteDetailsEvent
    data class Share(val content: String) : NoteDetailsEvent
    data class Copy(val content: String) : NoteDetailsEvent
}

sealed interface NoteDetailsIntent {
    data object Back : NoteDetailsIntent
    data object Save : NoteDetailsIntent
    data object Delete : NoteDetailsIntent
    data object Share : NoteDetailsIntent
    data object Copy : NoteDetailsIntent
}

class NoteDetailsViewModel(
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<Screen.NoteDetails>()
    var noteId = route.noteId
    val readModeInitially = noteId != null

    private var saveJob: Job? = null

    private val _events = eventChannel<NoteDetailsEvent>()
    val events = _events.receiveAsFlow()

    private var noteCreationDate: LocalDateTime? = null
    val contentField = TextFieldState()
    val subjectNameField = TextFieldState()
    val completeBeforeDate = MutableStateFlow<LocalDateTime?>(null)
    val priority = MutableStateFlow(0)

    val loading = flow {
        noteId?.let { id ->
            notesRepository.getNote(noteId = id).also {
                contentField.setTextAndPlaceCursorAtEnd(it.content)
                subjectNameField.setTextAndPlaceCursorAtEnd(it.subjectName)
                priority.value = it.priority
                noteCreationDate = it.createDate
                completeBeforeDate.value = it.completeBeforeDate
                emit(false)
            }
        } ?: run {
            subjectNameField.setTextAndPlaceCursorAtEnd(route.subjectName!!)
            emit(false)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    fun onIntent(intent: NoteDetailsIntent) {
        when (intent) {
            NoteDetailsIntent.Back -> navigateBackWithSaving()
            NoteDetailsIntent.Save -> save()
            NoteDetailsIntent.Copy -> _events.trySend(NoteDetailsEvent.Copy(contentField.text.toString()))
            NoteDetailsIntent.Delete -> _events.trySend(NoteDetailsEvent.Delete)
            NoteDetailsIntent.Share -> _events.trySend(NoteDetailsEvent.Share(contentField.text.toString()))
        }
    }

    private fun navigateBackWithSaving() {
        if (contentField.text.isEmpty() && noteId == null) {
            _events.trySend(NoteDetailsEvent.Back)
            return
        }
        if (saveJob?.isActive == true) return
        saveJob = viewModelScope.launch {
            noteId = notesRepository.saveNote(getComposedNote())
            _events.send(NoteDetailsEvent.Back)
        }
    }

    private fun save() {
        if (contentField.text.isEmpty() && noteId == null) return

        if (saveJob?.isActive == true) return
        saveJob = viewModelScope.launch {
            noteId = notesRepository.saveNote(getComposedNote())
        }
    }

    private fun getComposedNote(): Note {
        return Note(
            id = noteId,
            subjectName = if (noteId != null) {
                subjectNameField.text.toString()
            } else requireNotNull(route.subjectName) {
                "Subject name must be specified when navigating to note without id (creating note)"
            },
            content = contentField.text.toString(),
            priority = priority.value,
            createDate = DateTimeUtil.currentDateTime,
            isCompleted = false,
            completeBeforeDate = completeBeforeDate.value
        )
    }
}