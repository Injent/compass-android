package ru.bgitu.components.updates.api.model

interface UpdateInfo

data class NativeUpdateInfo(
    val availableVersionCode: Long,
    val totalBytesToDownload: Long,
    val forced: Boolean,
    val downloadUrl: String,
    val updateAvailability: UpdateAvailability,
    val checksum: String,
) : UpdateInfo

data object UnknownUpdateInfo : UpdateInfo