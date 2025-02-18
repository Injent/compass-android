package ru.bgitu.core.navigation

import androidx.navigation.NavOptionsBuilder

sealed interface NavigationAction {

    data class Navigate(
        val destination: Any,
        val navOptions: NavOptionsBuilder.() -> Unit = {}
    ): NavigationAction

    data object NavigateUp: NavigationAction
}