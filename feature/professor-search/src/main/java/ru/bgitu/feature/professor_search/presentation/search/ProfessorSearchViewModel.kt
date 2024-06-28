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
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.data.repository.CompassRepository
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.designsystem.util.textAsFlow

sealed class ProfessorSearchIntent {
    data class SelectProfessor(val professorName: String) : ProfessorSearchIntent()
    data object SeenAlert : ProfessorSearchIntent()
    data object ClearSearch : ProfessorSearchIntent()
    data class SetStartDestination(val professorName: String? = null) : ProfessorSearchIntent()
}

sealed class ProfessorSearchEvent {
    data class ShowError(val errorDetails: TextResource) : ProfessorSearchEvent()
    data class NavigateToProfessorDetails(val professorName: String) : ProfessorSearchEvent()
}

data class ProfessorSearchUiState internal constructor(
    val recentSearchResults: List<String> = emptyList(),
    val searchResults: List<String> = emptyList(),
    val seenScheduleAlert: Boolean = true,
    val isLoading: Boolean = true
)

class ProfessorSearchViewModel(
    compassRepository: CompassRepository,
    professorName: String? = null,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    var professorName = professorName
        private set

    private val _events = eventChannel<ProfessorSearchEvent>()
    val events = _events.receiveAsFlow()

    val searchFieldState = TextFieldState()
    @OptIn(FlowPreview::class)
    private val searchResults: Flow<List<String>> = searchFieldState
        .textAsFlow()
        .debounce(300)
        .mapLatest { query ->
            if (query.isEmpty() || currentCoroutineContext().isActive.not()) {
                return@mapLatest emptyList()
            }

            compassRepository.searchProfessor(query)
                .getOrElse {
                    return@mapLatest emptyList()
                }
                .distinct()
        }

    val uiState = combine(
        searchResults,
        compassRepository.getRecentProfessors(),
        settingsRepository.metadata
    ) { results, recentResults, metadata ->
        ProfessorSearchUiState(
            searchResults = results,
            recentSearchResults = recentResults,
            isLoading = false,
            seenScheduleAlert = metadata.seenTeacherScheduleAlert
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfessorSearchUiState()
        )

    fun onIntent(intent: ProfessorSearchIntent) {
        when (intent) {
            is ProfessorSearchIntent.SelectProfessor -> {
                _events.trySend(
                    ProfessorSearchEvent.NavigateToProfessorDetails(intent.professorName)
                )
            }
            ProfessorSearchIntent.SeenAlert -> {
                viewModelScope.launch {
                    settingsRepository.updateMetadata {
                        it.copy(seenTeacherScheduleAlert = true)
                    }
                }
            }
            ProfessorSearchIntent.ClearSearch -> {
                viewModelScope.launch {
                    settingsRepository.updateMetadata {
                        it.copy(recentProfessorSearch = emptyList())
                    }
                }
            }
            is ProfessorSearchIntent.SetStartDestination -> {
                professorName = intent.professorName
            }
        }
    }
}