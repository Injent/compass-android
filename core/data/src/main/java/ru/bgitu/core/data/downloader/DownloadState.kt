package ru.bgitu.core.data.downloader

sealed class DownloadState {
    data class Downloading(
        val totalBytesToDownload: Long,
        val bytesDownloaded: Long
    ) : DownloadState()

    data object Finished : DownloadState()

    data class Failed(val error: Throwable? = null) : DownloadState()
}