package ru.bgitu.feature.notes.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.feature.notes.data.NotesRepository

sealed interface NotesIntent {
    data class SetCompleted(val noteId: Int, val completed: Boolean) : NotesIntent
    data class SelectTab(val tabIndex: Int) : NotesIntent
    data class Delete(val noteId: Int) : NotesIntent
}

class NotesViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()

    private val lazyCompletedNotesKey = MutableStateFlow(false)
    private val lazyDeletedNotesKey = MutableStateFlow(false)

    val groupedNotes = notesRepository.uncompletedNotes
        .mapLatest { notes ->
            notes.groupBy { it.subjectName }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    val completedNotes = combine(
        notesRepository.completedNotes,
        lazyCompletedNotesKey
    ) { notes, init ->
        if (init) notes else emptyList()
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val deletedNotes = combine(
        notesRepository.deletedNotes,
        lazyDeletedNotesKey
    ) { notes, init ->
        if (init) notes else emptyList()
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onIntent(intent: NotesIntent) {
        when (intent) {
            is NotesIntent.SetCompleted -> viewModelScope.launch {
                notesRepository.setCompleted(intent.noteId, intent.completed)
            }
            is NotesIntent.SelectTab -> {
                _selectedTabIndex.value = intent.tabIndex

                when (intent.tabIndex) {
                    1 -> lazyCompletedNotesKey.value = true
                    2 -> lazyDeletedNotesKey.value = true
                }
            }
            is NotesIntent.Delete -> viewModelScope.launch {
                notesRepository.putIntoBin(noteId = intent.noteId)
            }
        }
    }
}