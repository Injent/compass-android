package ru.bgitu.core.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val accessToken: String,
    val refreshToken: String
)