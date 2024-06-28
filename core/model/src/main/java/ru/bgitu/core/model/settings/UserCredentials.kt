package ru.bgitu.core.model.settings

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.plus
import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val accessToken: String,
    val refreshToken: String,
    val expirationDate: Instant,
    val authDate: Instant = Clock.System.now(),
) {
    fun measureDateDiff(): Long {
        return Clock.System.now().toEpochMilliseconds() - authDate.toEpochMilliseconds()
    }

    val isTokenExpired: Boolean
        get() = expirationDate < Clock.System.now().plus(
            authDate.epochSeconds - Clock.System.now().epochSeconds,
            DateTimeUnit.SECOND
        )
}
