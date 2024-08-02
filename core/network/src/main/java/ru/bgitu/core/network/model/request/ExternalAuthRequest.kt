package ru.bgitu.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ExternalAuthRequest(
    val authMethod: String,
    val authCode: String,
    val fullName: String,
    val avatarUrl: String?
)