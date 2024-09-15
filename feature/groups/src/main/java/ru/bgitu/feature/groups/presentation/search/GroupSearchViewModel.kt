package ru.bgitu.feature.groups.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.common.eventChannel
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.designsystem.util.textAsFlow
import ru.bgitu.core.model.Group
import ru.bgitu.feature.groups.R
import ru.bgitu.feature.groups.data.GroupManagementRepository

data class GroupSearchUiState(
    val results: List<Group> = emptyList()
)

sealed interface GroupSearchEvent {
    data class ShowSnackbarError(val msg: TextResource) : GroupSearchEvent
}

class GroupSearchViewModel(
    private val groupRepository: GroupManagementRepository
) : ViewModel() {
    private val _events = eventChannel<GroupSearchEvent.ShowSnackbarError>()
    val events = _events.receiveAsFlow()

    val searchFieldState = TextFieldState()

    @OptIn(FlowPreview::class)
    val uiState = combine(
        searchFieldState.textAsFlow().debounce(400),
        groupRepository.getGroupsData().mapLatest { it.savedGroups + it.primaryGroup }
    ) { query, existingGroups ->
        if (query.isEmpty()) {
            return@combine GroupSearchUiState()
        }

        GroupSearchUiState(
            results = groupRepository.searchGroup(query)
                .getOrElse {
                    _events.trySend(GroupSearchEvent.ShowSnackbarError(it.details))
                    emptyList()
                }
                .filterNot { group ->
                    group in existingGroups
                }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = GroupSearchUiState()
        )
}
