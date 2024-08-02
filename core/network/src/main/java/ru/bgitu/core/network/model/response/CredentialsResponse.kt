package ru.bgitu.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CredentialsResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long
)