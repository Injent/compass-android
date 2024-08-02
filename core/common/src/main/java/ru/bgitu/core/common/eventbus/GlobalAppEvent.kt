package ru.bgitu.core.common.eventbus

sealed interface GlobalAppEvent {
    data object SignOut : GlobalAppEvent
    data object ChangeGroup : GlobalAppEvent
}