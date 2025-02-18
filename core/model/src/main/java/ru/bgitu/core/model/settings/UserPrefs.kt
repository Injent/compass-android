package ru.bgitu.core.model.settings

import ru.bgitu.core.model.Group

data class UserPrefs(
    val theme: UiTheme,
    val showPinnedSchedule: Boolean,
    val teacherFilterByDays: Boolean,
    val savedGroups: List<Group>,
    val showGroupsOnMainScreen: Boolean,
    val helpSiteTraffic: Boolean,
    val useDynamicTheme: Boolean,
    val subscribedTopics: List<SubscribedTopic>,
    val notificationDelegationEnabled: Boolean
)
