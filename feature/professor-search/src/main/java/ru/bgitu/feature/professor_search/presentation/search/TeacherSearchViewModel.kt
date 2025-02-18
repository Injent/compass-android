package ru.bgitu.feature.professor_search.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.data.repository.CompassRepository
import ru.bgitu.core.data.util.NetworkMonitor
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.designsystem.util.textAsFlow

sealed class TeacherSearchIntent {
    data class SelectTeacher(val professorName: String) : TeacherSearchIntent()
    data object SeenAlert : TeacherSearchIntent()
    data object ClearSearch : TeacherSearchIntent()
}

sealed class TeacherSearchEvent {
    data class ShowError(val errorDetails: TextResource) : TeacherSearchEvent()
    data class NavigateToTeacherDetails(val teacherName: String) : TeacherSearchEvent()
}

data class TeacherSearchUiState(
    val recentSearchResults: List<String> = emptyList(),
    val searchResults: List<String> = emptyList(),
    val seenScheduleAlert: Boolean = true,
    val isLoading: Boolean = true
)

class TeacherSearchViewModel(
    compassRepository: CompassRepository,
    networkMonitor: NetworkMonitor,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _events = eventChannel<TeacherSearchEvent>()
    val events = _events.receiveAsFlow()

    val searchFieldState = TextFieldState()
    @OptIn(FlowPreview::class)
    private val searchResults: Flow<List<String>> = combine(
        networkMonitor.isOnline,
        searchFieldState.textAsFlow().debounce(300)
    ) { _, query ->
        if (query.isEmpty() || currentCoroutineContext().isActive.not()) {
            return@combine emptyList()
        }

        compassRepository.searchProfessor(query)
            .getOrElse {
                _events.trySend(TeacherSearchEvent.ShowError(it.details))
                return@combine emptyList()
            }
            .distinct()
    }

    val uiState = combine(
        searchResults,
        compassRepository.getRecentProfessors(),
        settingsRepository.metadata
    ) { results, recentResults, metadata ->
        TeacherSearchUiState(
            searchResults = results,
            recentSearchResults = recentResults,
            isLoading = false,
            seenScheduleAlert = metadata.seenTeacherScheduleAlert
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TeacherSearchUiState()
        )

    fun onIntent(intent: TeacherSearchIntent) {
        when (intent) {
            is TeacherSearchIntent.SelectTeacher -> {
                _events.trySend(
                    TeacherSearchEvent.NavigateToTeacherDetails(intent.professorName)
                )
            }
            TeacherSearchIntent.SeenAlert -> {
                viewModelScope.launch {
                    settingsRepository.updateMetadata {
                        it.copy(seenTeacherScheduleAlert = true)
                    }
                }
            }
            TeacherSearchIntent.ClearSearch -> {
                viewModelScope.launch {
                    settingsRepository.updateMetadata {
                        it.copy(recentProfessorSearch = emptyList())
                    }
                }
            }
        }
    }
}