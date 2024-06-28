package ru.bgitu.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterEosUserRequest(
    val userId: Long,
    val eosUserId: Long,
    val eosGroupName: String,
    val fullName: String,
    val avatarUrl: String
)
