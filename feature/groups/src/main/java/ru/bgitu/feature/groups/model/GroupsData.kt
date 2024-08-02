package ru.bgitu.feature.groups.model

import ru.bgitu.core.model.Group

data class GroupsData(
    val primaryGroup: Group?,
    val savedGroups: List<Group>,
    val showGroupsOnMainScreen: Boolean
)
