package ru.bgitu.core.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable data object MainGraph : Screen

    @Serializable data object FindMateGraph : Screen

    @Serializable data object ProfileGraph : Screen

    @Serializable data object Loading : Screen

    @Serializable data object About : Screen

    @Serializable data object SearchMate : Screen

    @Serializable data object Help : Screen

    @Serializable data object Home : Screen

    @Serializable data class Login(val compactScreen: Boolean = false) : Screen

    @Serializable data class TeacherSearch(val teacherName: String? = null) : Screen

    @Serializable data object Profile : Screen

    @Serializable data object ProfileSettings : Screen

    @Serializable data object Settings : Screen

    @Serializable data object Groups : Screen

    @Serializable data class GroupSearch(val resultKey: String) : Screen

    @Serializable data object ExpertApply : Screen

    @Serializable data class VariantScreen(val subjectName: String) : Screen

    @Serializable data object Onboarding : Screen

    @Serializable data class Notes(val noteId: Int?) : Screen

    @Serializable data class NoteDetails(val noteId: Int? = null, val subjectName: String? = null) : Screen
}