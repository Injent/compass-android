package ru.bgitu.core.datastore.model

object UnseenFeatures {
    const val TURN_OFF_DYNAMIC_THEME = 1
    const val ADD_WIDGET = 2
    const val NEW_CHANGELOG = 3

    val PROFILE_FEATURES = setOf(
        ADD_WIDGET,
        NEW_CHANGELOG
    )
}