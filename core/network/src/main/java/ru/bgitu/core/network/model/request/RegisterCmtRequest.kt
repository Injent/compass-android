package ru.bgitu.core.network.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterCmtRequest(
    @SerialName("tokenType")
    val type: String,
    val token: String
)