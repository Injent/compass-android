package ru.bgitu.core.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable data object MainGraph : Screen

    @Serializable data object LoginGraph : Screen

    @Serializable data object FindMateGraph : Screen

    @Serializable data object ProfileGraph : Screen

    @Serializable data object Loading : Screen

    @Serializable data object About : Screen

    @Serializable data object SearchMate : Screen

    @Serializable data object Help : Screen

    @Serializable data object Home : Screen

    @Serializable data class Login(val compactScreen: Boolean = false) : Screen

    @Serializable data object Recovery : Screen

    @Serializable data class PickGroup(val predictedSearchQuery: String? = null) : Screen

    @Serializable data class ProfessorSearch(val professorName: String? = null) : Screen

    @Serializable data class ProfessorSchedule(val professorName: String) : Screen

    @Serializable data object Profile : Screen

    @Serializable data object ProfileSettings : Screen

    @Serializable data object Settings : Screen

    @Serializable data object Groups : Screen

    @Serializable data class GroupSearch(val backResultType: String) : Screen
}