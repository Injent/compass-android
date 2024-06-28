package ru.bgitu.core.common.eventbus

sealed interface GlobalAppEvent {
    data object SignOut : GlobalAppEvent
    data class ChangeGroup(val groupId: Int) : GlobalAppEvent
}