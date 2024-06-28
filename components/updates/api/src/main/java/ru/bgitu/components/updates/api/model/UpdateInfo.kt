package ru.bgitu.components.updates.api.model

sealed class UpdateInfo {
    data class NativeUpdate(
        val availableVersionCode: Long,
        val totalBytesToDownload: Long,
        val forced: Boolean,
        val downloadUrl: String,
        val updateAvailability: UpdateAvailability,
        val checksum: String,
    ) : UpdateInfo()

    data class RuStoreUpdate(
        val availableVersionCode: Long,
        val updateAvailability: UpdateAvailability,
    ) : UpdateInfo()

    data object Unknown : UpdateInfo()
}