package ru.bgitu.core.model.settings

import ru.bgitu.core.model.Group

data class UserPrefs(
    val theme: UiTheme,
    val showPinnedSchedule: Boolean,
    val ignoreMinorUpdates: Boolean,
    val teacherSortByWeeks: Boolean,
    val savedGroups: List<Group>,
    val showGroupsOnMainScreen: Boolean
)
