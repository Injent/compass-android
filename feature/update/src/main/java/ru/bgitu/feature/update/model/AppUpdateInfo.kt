package ru.bgitu.feature.update.model

sealed interface AppUpdateInfo {
    data class Success(
        val sizeBytes: Long,
        val changelog: String,
        val required: Boolean,
    ) : AppUpdateInfo

    data object Empty : AppUpdateInfo
    data object Loading : AppUpdateInfo
}