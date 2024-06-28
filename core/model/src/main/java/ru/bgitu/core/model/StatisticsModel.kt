package ru.bgitu.core.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class StatisticsModel(
    val userId: Long,
    val apiVersion: Int,
    val groupName: String,
    val lastActivity: LocalDate,
    val data: Data
) {
    @Serializable
    data class Data(
        val deviceModel: String
    )
}
