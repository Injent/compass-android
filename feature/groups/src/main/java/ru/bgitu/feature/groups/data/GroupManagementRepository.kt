package ru.bgitu.feature.groups.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.bgitu.core.common.eventbus.EventBus
import ru.bgitu.core.common.eventbus.GlobalAppEvent
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.Group
import ru.bgitu.core.network.CompassService
import ru.bgitu.feature.groups.model.GroupsData

class GroupManagementRepository(
    private val settingsRepository: SettingsRepository,
    private val compassService: CompassService
) {

    fun getGroupsData(): Flow<GroupsData> {
        return settingsRepository.data.map {
            GroupsData(
                primaryGroup = it.primaryGroup,
                savedGroups = it.userPrefs.savedGroups,
                showGroupsOnMainScreen = it.userPrefs.showGroupsOnMainScreen
            )
        }
    }

    suspend fun setPrimaryGroup(group: Group) {
        settingsRepository.updateUserPrefs {
            it.copy(savedGroups = it.savedGroups.filterNot { savedGroup -> savedGroup == group })
        }
        settingsRepository.setGroup(group)
        EventBus.post(GlobalAppEvent.ChangePrimaryGroup)
    }

    suspend fun searchGroup(query: String) = compassService.searchGroups(query.replace("-", ""))

    suspend fun setGroupsVisibility(visible: Boolean) {
        settingsRepository.updateUserPrefs {
            it.copy(showGroupsOnMainScreen = visible)
        }
    }

    suspend fun setSavedGroups(savedGroups: List<Group>) {
        settingsRepository.updateUserPrefs {
            it.copy(savedGroups = savedGroups)
        }
    }

    suspend fun addGroup(group: Group) {
        settingsRepository.updateUserPrefs {
            it.copy(
                savedGroups = it.savedGroups + group
            )
        }
    }

    suspend fun removeGroup(group: Group) {
        settingsRepository.updateUserPrefs {
            it.copy(
                savedGroups = it.savedGroups.filterNot {
                    savedGroup -> savedGroup.id == group.id
                }
            )
        }
    }
}