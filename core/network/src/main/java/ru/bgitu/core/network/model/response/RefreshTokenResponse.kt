package ru.bgitu.core.network.model.response

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    @SerialName("tokenExpireAt")
    val expirationDate: Instant,
    val requestDate: Instant = Instant.fromEpochMilliseconds(0)
)
