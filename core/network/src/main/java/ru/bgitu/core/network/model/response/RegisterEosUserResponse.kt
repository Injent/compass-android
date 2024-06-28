package ru.bgitu.core.network.model.response

import kotlinx.serialization.Serializable
import ru.bgitu.core.model.settings.UserCredentials

@Serializable
data class RegisterEosUserResponse(
    val userId: Long,
    val eosUserId: Long,
    val groupId: Int?,
    val credentials: UserCredentials
)