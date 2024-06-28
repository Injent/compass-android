package ru.bgitu.feature.login.presentation.pickgroup

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.core.data.util.NetworkMonitor
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.data.model.SignInResult
import ru.bgitu.core.data.repository.CompassAuthenticator
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.designsystem.util.textAsFlow
import ru.bgitu.core.model.Group

data class PickGroupUiState(
    val searchResults: List<Group> = emptyList(),
    val selectedGroup: Group? = null,
)

sealed class PickGroupIntent {
    data class PickGroup(val group: Group) : PickGroupIntent()
    data object ResetGroup : PickGroupIntent()
    data object Continue : PickGroupIntent()
    data object Back : PickGroupIntent()
}

sealed class PickGroupEvent {
    data object SuccessfulAuthentication : PickGroupEvent()
    data class Error(val details: TextResource) : PickGroupEvent()
    data object HideSnackbar : PickGroupEvent()
    data object Back : PickGroupEvent()
}

private const val MAX_ITEM_SEARCH_COUNT = 15

class PickGroupViewModel(
    private val compassAuthenticator: CompassAuthenticator,
    private val syncManager: SyncManager,
    networkMonitor: NetworkMonitor,
    searchQuery: String? = null
) : ViewModel() {
    private val fromEosAuth = searchQuery != null
    private val _events = eventChannel<PickGroupEvent>()
    val events = _events.receiveAsFlow()

    private val selectedGroup = MutableStateFlow<Group?>(null)
    val searchFieldState = TextFieldState(initialText = searchQuery ?: "")

    @OptIn(FlowPreview::class)
    val uiState = pickGroupUiState(
        compassAuthenticator = compassAuthenticator,
        networkMonitor = networkMonitor,
        searchQuery = searchFieldState
            .textAsFlow()
            .debounce(300),
        selectedGroup = selectedGroup,
        onEvent = _events::trySend
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PickGroupUiState()
        )

    fun onIntent(intent: PickGroupIntent) {
        when (intent) {
            is PickGroupIntent.PickGroup -> {
                selectedGroup.value = intent.group
            }
            PickGroupIntent.ResetGroup -> {
                selectedGroup.value = null
                searchFieldState.clearText()
                _events.trySend(PickGroupEvent.HideSnackbar)
            }
            PickGroupIntent.Continue -> {
                selectedGroup.value?.let { group ->
                    viewModelScope.launch {
                        if (fromEosAuth) {
                            eosAuthWithGroup(group = group)
                        } else {
                            pickGroupAndAuth(group = group)
                        }
                    }
                }
            }
            PickGroupIntent.Back -> {
                _events.trySend(PickGroupEvent.Back)
            }
        }
    }

    private suspend fun pickGroupAndAuth(group: Group) {
        when (val registrationResult = compassAuthenticator.registerGuest(group)) {
            SignInResult.Success -> {
                syncManager.fullSync()

                _events.send(PickGroupEvent.SuccessfulAuthentication)
            }
            is SignInResult.Error -> {
                _events.send(PickGroupEvent.Error(registrationResult.details))
            }
            else -> Unit
        }
    }

    private suspend fun eosAuthWithGroup(
        group: Group,
    ) {
        compassAuthenticator.chooseGroup(group)
            .onSuccess {
                syncManager.fullSync()
                _events.send(PickGroupEvent.SuccessfulAuthentication)
            }
            .onFailure {
                _events.send(PickGroupEvent.Error(it.details))
            }
    }
}

private fun pickGroupUiState(
    compassAuthenticator: CompassAuthenticator,
    networkMonitor: NetworkMonitor,
    searchQuery: Flow<String>,
    selectedGroup: Flow<Group?>,
    onEvent: (PickGroupEvent) -> Unit
): Flow<PickGroupUiState> {
    return combine(
        networkMonitor.isOnline,
        searchQuery,
        selectedGroup
    ) { isOnline, query, group ->
        val optimizedQuery = query.replace(Regex("[^а-яА-ЯёЁ0-9]"), "")

        val searchResults = if (optimizedQuery.isNotEmpty()) {
             compassAuthenticator.searhGroups(optimizedQuery)
                .getOrElse {
                    if (isOnline)
                        onEvent(PickGroupEvent.Error(it.details))
                    emptyList()
                }
                .take(MAX_ITEM_SEARCH_COUNT)
        } else emptyList()

        PickGroupUiState(
            searchResults = searchResults,
            selectedGroup = group,
        )
    }
}