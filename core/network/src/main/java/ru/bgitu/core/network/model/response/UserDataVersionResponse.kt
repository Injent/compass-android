package ru.bgitu.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserDataVersionResponse(
    val userDataVersion: Int
)
