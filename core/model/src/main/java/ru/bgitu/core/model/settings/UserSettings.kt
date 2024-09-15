package ru.bgitu.core.model.settings

import ru.bgitu.core.model.Group
import ru.bgitu.core.model.UserProfile

data class UserSettings(
    val userPrefs: UserPrefs,
    val userId: Long,
    val groupId: Int?,
    val groupName: String?,
    val currentAppVersionCode: Int,
    val newFeaturesVersion: Int,
    val isAuthorized: Boolean,
    val userProfile: UserProfile?,
    val isAnonymous: Boolean,
    val shouldShowOnboarding: Boolean,
    val shouldShowDataResetAlert: Boolean
) {
    val primaryGroup: Group?
        get() = if (groupId != null && groupName != null) {
            Group(groupId, groupName)
        } else null
}