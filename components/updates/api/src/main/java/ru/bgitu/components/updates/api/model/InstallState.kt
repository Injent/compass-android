package ru.bgitu.components.updates.api.model

import ru.bgitu.core.common.TextResource

sealed class InstallState private constructor() {

    data class Downloading(
        val bytesDownloaded: Long,
        val totalBytesDownloaded: Long,
    ) : InstallState()

    data object Unknown : InstallState()

    data object Downloaded : InstallState()

    data class Failed(val details: TextResource) : InstallState()

    data object NothingToInstall : InstallState()
}