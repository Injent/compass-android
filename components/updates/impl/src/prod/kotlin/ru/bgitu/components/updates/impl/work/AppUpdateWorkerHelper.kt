package ru.bgitu.components.updates.impl.work

import androidx.core.net.toUri
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.WorkInfo
import androidx.work.workDataOf
import ru.bgitu.core.data.downloader.DownloadState
import java.io.File

val UpdateConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

internal const val PARAM_URL = "url"
internal const val PARAM_DESTINATION = "destination"
internal const val PARAM_TOTAL_BYTES_TO_DOWNLOAD = "total_bytes_to_download"
internal const val PARAM_BYTES_DOWNLOADED = "bytes_downloaded"
internal const val PARAM_DOWNLOAD_STATE = "download_state"

internal const val RESULT_PROGRESS = 0
internal const val RESULT_FAILURE = 1
internal const val RESULT_SUCCESS = -1

internal fun ListenableWorker.setDownloadProgressAsync(state: DownloadState) {
    val data = when (state) {
        is DownloadState.Downloading -> {
            Data.Builder()
                .putInt(PARAM_DOWNLOAD_STATE, RESULT_PROGRESS)
                .putLong(PARAM_BYTES_DOWNLOADED, state.bytesDownloaded)
                .putLong(PARAM_TOTAL_BYTES_TO_DOWNLOAD, state.totalBytesToDownload)
                .build()
        }
        is DownloadState.Failed -> {
            Data.Builder()
                .putInt(PARAM_DOWNLOAD_STATE, RESULT_FAILURE)
                .build()
        }
        DownloadState.Finished -> {
            Data.Builder()
                .putInt(PARAM_DOWNLOAD_STATE, RESULT_SUCCESS)
                .build()
        }
    }
    this.setProgressAsync(data)
}

internal fun DownloadState.toProgress(): Data {
    return when (this) {
        is DownloadState.Downloading -> {
            Data.Builder()
                .putInt(PARAM_DOWNLOAD_STATE, RESULT_PROGRESS)
                .putLong(PARAM_BYTES_DOWNLOADED, bytesDownloaded)
                .putLong(PARAM_TOTAL_BYTES_TO_DOWNLOAD, totalBytesToDownload)
                .build()
        }
        is DownloadState.Failed -> {
            Data.Builder()
                .putInt(PARAM_DOWNLOAD_STATE, RESULT_FAILURE)
                .build()
        }
        DownloadState.Finished -> {
            Data.Builder()
                .putInt(PARAM_DOWNLOAD_STATE, RESULT_SUCCESS)
                .build()
        }
    }
}

internal fun WorkInfo.getDownloadProgress(): DownloadState {
    return when (progress.getInt(PARAM_DOWNLOAD_STATE, RESULT_FAILURE)) {
        RESULT_PROGRESS -> DownloadState.Downloading(
            totalBytesToDownload = progress.getLong(PARAM_TOTAL_BYTES_TO_DOWNLOAD, 0),
            bytesDownloaded = progress.getLong(PARAM_BYTES_DOWNLOADED, 0)
        )
        RESULT_SUCCESS -> DownloadState.Finished
        else -> DownloadState.Failed()
    }
}

fun createAppUpdateWorkerParams(url: String, destinationInCache: File): Data {
    return workDataOf(
        PARAM_URL to url,
        PARAM_DESTINATION to destinationInCache.toUri().toString()
    )
}