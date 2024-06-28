package ru.bgitu.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject

@Serializable
data class CompassAccount(
    val userId: Long,
    val eosUserId: Long,
    val groupId: Int?,
    val groupName: String?,
    val fullName: String,
    val avatarUrl: String?,
    val role: UserRole,
    val permissions: List<UserPermission>,
    @SerialName("additionalData")
    val data: JsonElement = buildJsonObject {  }
)