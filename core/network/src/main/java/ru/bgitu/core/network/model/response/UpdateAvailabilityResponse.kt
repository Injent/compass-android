package ru.bgitu.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAvailabilityResponse(
    val versionCode: Long,
    val downloadUrl: String,
    val size: Long,
    val checksum: String
)