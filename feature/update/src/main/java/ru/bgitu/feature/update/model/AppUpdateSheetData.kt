package ru.bgitu.feature.update.model

data class AppUpdateSheetData(
    val sizeBytes: Long,
    val forced: Boolean,
    val availableVersionCode: Long
)