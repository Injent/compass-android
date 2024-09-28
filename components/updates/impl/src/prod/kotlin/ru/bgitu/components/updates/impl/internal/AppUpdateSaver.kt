package ru.bgitu.components.updates.impl.internal

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ru.bgitu.components.updates.api.model.InstallState
import ru.bgitu.components.updates.impl.R
import ru.bgitu.components.updates.impl.work.AppUpdateWorker
import ru.bgitu.components.updates.impl.work.getDownloadProgress
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.data.downloader.DownloadState
import java.io.File

internal class AppUpdateSaver(
    context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    private fun getProgressOfWork(versionCode: Long): Flow<InstallState> = workManager
        .getWorkInfosByTagFlow(versionCode.toString())
        .map {
            if (it.isEmpty())
                InstallState.NothingToInstall
            else when (it.first().state) {
                WorkInfo.State.RUNNING -> {
                    val downloadState = it.first().getDownloadProgress()
                    if (downloadState is DownloadState.Downloading) {
                        InstallState.Downloading(
                            bytesToDownload = downloadState.totalBytesToDownload,
                            totalBytesDownloaded = downloadState.bytesDownloaded
                        )
                    } else InstallState.NothingToInstall
                }
                WorkInfo.State.SUCCEEDED -> InstallState.Downloaded
                WorkInfo.State.FAILED, WorkInfo.State.CANCELLED -> {
                    InstallState.Failed(
                        TextResource.Id(R.string.error_abort)
                    )
                }
                else -> InstallState.Unknown
            }
        }
        .onStart { emit(InstallState.Unknown) }

    fun downloadAndSave(
        versionCode: Long,
        url: String,
        destination: File
    ): Flow<InstallState> {
        workManager.enqueueUniqueWork(
            AppUpdateWorkName,
            ExistingWorkPolicy.KEEP,
            AppUpdateWorker.startUpdateWork(
                versionCode = versionCode,
                url = url,
                destination = destination
            )
        )
        return getProgressOfWork(versionCode)
    }
}

private const val AppUpdateWorkName = "app_update_work"