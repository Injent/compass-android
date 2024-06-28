package ru.bgitu.feature.groups.presentation.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.core.model.Group
import ru.bgitu.feature.groups.data.GroupManagementRepository

sealed interface GroupsUiState {
    data class Success(
        val primaryGroup: Group,
        val savedGroups: List<Group>,
        val showGroupsOnMainScreen: Boolean,
    ) : GroupsUiState

    data object Error : GroupsUiState

    data object Loading : GroupsUiState
}

sealed interface GroupsIntent {
    data class ChangePrimaryGroup(val group: Group) : GroupsIntent
    data class SetGroups(val groups: List<Group>) : GroupsIntent
    data class AddGroup(val group: Group) : GroupsIntent
    data class RemoveGroup(val group: Group) : GroupsIntent
    data class SetGroupVisibility(val visible: Boolean) : GroupsIntent
}

class GroupsViewModel(
    private val groupRepository: GroupManagementRepository,
) : ViewModel() {

    val uiState = groupRepository.getGroupsData().mapLatest { groupData ->
        GroupsUiState.Success(
            primaryGroup = groupData.primaryGroup,
            savedGroups = groupData.savedGroups,
            showGroupsOnMainScreen = groupData.showGroupsOnMainScreen
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = GroupsUiState.Loading
        )

    fun onIntent(intent: GroupsIntent) {
        when (intent) {
            is GroupsIntent.AddGroup -> {
                viewModelScope.launch {
                    groupRepository.addGroup(intent.group)
                }
            }
            is GroupsIntent.SetGroups -> {
                viewModelScope.launch {
                    groupRepository.setSavedGroups(intent.groups)
                }
            }
            is GroupsIntent.ChangePrimaryGroup -> {
                viewModelScope.launch {
                    groupRepository.setPrimaryGroup(intent.group)
                }
            }
            is GroupsIntent.RemoveGroup -> {
                viewModelScope.launch {
                    groupRepository.removeGroup(intent.group)
                }
            }
            is GroupsIntent.SetGroupVisibility -> {
                viewModelScope.launch {
                    groupRepository.setGroupsVisibility(intent.visible)
                }
            }
        }
    }
}
