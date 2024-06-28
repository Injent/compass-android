package ru.bgitu.core.network.model.response

import kotlinx.serialization.Serializable
import ru.bgitu.core.model.settings.UserCredentials

@Serializable
data class RegisterGuestAuthResponse(
    val userId: Long,
    val groupId: Int,
    val groupName: String,
    val credentials: UserCredentials
)