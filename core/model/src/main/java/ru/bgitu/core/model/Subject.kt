package ru.bgitu.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val subjectId: Int,
    val name: String
)