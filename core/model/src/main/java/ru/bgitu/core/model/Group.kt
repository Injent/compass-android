package ru.bgitu.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: Int,
    val name: String
)