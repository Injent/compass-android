package ru.bgitu.feature.groups.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.Group
import ru.bgitu.core.network.CompassService
import ru.bgitu.feature.groups.model.GroupsData

class GroupManagementRepository(
    private val settingsRepository: SettingsRepository,
    private val serviceApi: CompassService
) {

    fun getGroupsData(): Flow<GroupsData> {
        return settingsRepository.data.mapLatest {
            GroupsData(
                primaryGroup = checkNotNull(it.primaryGroup) {
                    "Primary group is empty: groups screen reacher before authorization"
                },
                savedGroups = it.userPrefs.savedGroups,
                showGroupsOnMainScreen = it.userPrefs.showGroupsOnMainScreen
            )
        }
    }

    suspend fun setPrimaryGroup(group: Group) {
        settingsRepository.updateUserPrefs { prefs ->
            prefs.copy(
                savedGroups = prefs.savedGroups.filterNot { it == group }
            )
        }
        settingsRepository.setGroup(group)
    }

    suspend fun searchGroup(query: String) = serviceApi.searchGroups(query)

    suspend fun setGroupsVisibility(visible: Boolean) {
        settingsRepository.updateUserPrefs {
            it.copy(
                showGroupsOnMainScreen = visible
            )
        }
    }

    suspend fun changePrimaryGroup(group: Group) = serviceApi.chooseGroup(group)

    suspend fun setSavedGroups(savedGroups: List<Group>) {
        settingsRepository.updateUserPrefs {
            it.copy(
                savedGroups = savedGroups
            )
        }
    }

    suspend fun changeGroupPosition(from: Int, to: Int) {
        settingsRepository.updateUserPrefs {
            it.copy(
                savedGroups = it.savedGroups.toMutableList().apply {
                    add(to, removeAt(from))
                }
            )
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