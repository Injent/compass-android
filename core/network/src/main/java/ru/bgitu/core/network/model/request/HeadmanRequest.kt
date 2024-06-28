package ru.bgitu.core.network.model.request

import kotlinx.serialization.Serializable
import ru.bgitu.core.model.UserRole

@Serializable
data class HeadmanRequest(
    val role: UserRole,
    val base64photo: String,
    val comment: String
)