package ru.bgitu.feature.findmate.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.data.repository.CompassRepository
import ru.bgitu.core.designsystem.util.textAsFlow
import ru.bgitu.core.model.SearchMateItem

sealed class SearchMateIntent {
    data class OpenProfile(val userId: Int) : SearchMateIntent()
}

sealed class SearchMateEvent {

}

sealed class SearchMateUiState {
    data object NotFound : SearchMateUiState()
    data class Error(val details: TextResource) : SearchMateUiState()
    data class Success(val profiles: List<SearchMateItem>) : SearchMateUiState()
    data object Idle : SearchMateUiState()
}

class SearchMateViewModel(
    private val compassRepository: CompassRepository
) : ViewModel() {
    val searchField = TextFieldState()

    @OptIn(FlowPreview::class)
    val uiState = searchField.textAsFlow()
        .debounce(300)
        .mapLatest{ query ->
        if (query.isEmpty()) return@mapLatest SearchMateUiState.Idle

        val results = compassRepository.searchMates(query)
            .getOrElse { result ->
                return@mapLatest SearchMateUiState.Error(details = result.details)
            }

        if (results.isEmpty()) {
            return@mapLatest SearchMateUiState.NotFound
        }

        SearchMateUiState.Success(results)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchMateUiState.Idle
        )


    fun onIntent(intent: SearchMateIntent) {
        when (intent) {
            is SearchMateIntent.OpenProfile -> {

            }
        }
    }
}