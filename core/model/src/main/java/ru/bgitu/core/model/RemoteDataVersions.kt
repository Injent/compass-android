package ru.bgitu.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteDataVersions(
    val scheduleVersion: Int,
    @SerialName("forceUpdateVersion")
    val lastForceUpdateVersion: Int,
)
