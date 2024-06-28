package ru.bgitu.core.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EosAuthRequest(
    @SerialName("userName")
    val login: String,
    val password: String
)
