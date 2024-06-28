package ru.bgitu.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterGuestRequest(
    val appUUID: String,
    val groupId: Int,
    val groupName: String
)