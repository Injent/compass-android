package ru.bgitu.core.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ChooseGroupRequest(
    val groupId: Int,
    val groupName: String
)
