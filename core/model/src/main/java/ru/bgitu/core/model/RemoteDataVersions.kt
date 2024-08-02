package ru.bgitu.core.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteDataVersions(
    val scheduleVersion: Int
)
