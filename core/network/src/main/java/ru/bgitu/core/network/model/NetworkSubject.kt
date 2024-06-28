package ru.bgitu.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkSubject(
    @SerialName("id")
    val subjectId: Int,
    val name: String
)
