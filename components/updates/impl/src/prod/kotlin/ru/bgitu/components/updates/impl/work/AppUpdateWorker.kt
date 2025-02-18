package ru.bgitu.components.updates.impl.work

import android.content.Context
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.common.runResulting
import ru.bgitu.core.data.downloader.AndroidFileDownloader
import ru.bgitu.core.data.downloader.DownloadState
import ru.bgitu.core.notifications.Notifier
import java.io.File

class AppUpdateWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), KoinComponent {
    private val fileDownloader: AndroidFileDownloader by inject()
    private val notifier: Notifier by inject()

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            Notifier.BACKGROUND_NOTIFICATION_ID,
            notifier.backgroundWorkNotification()
        )
    }

    @OptIn(FlowPreview::class)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        runResulting {
            val url = requireNotNull(inputData.getString(PARAM_URL))
            val destination = requireNotNull(inputData.getString(PARAM_DESTINATION))
                .toUri()
                .toFile()

            var workerResult: Result = Result.failure()
            fileDownloader.downloadAndSaveFile(
                url = url,
                destination = destination,
            )
                .debounce(100)
                .collectLatest { state ->
                    setProgress(state.toProgress())

                    if (state is DownloadState.Finished) {
                        workerResult = Result.success()
                    }
                }

            workerResult
        }
            .getOrElse { Result.failure() }
    }

    companion object {
        fun startUpdateWork(
            versionCode: Long,
            url: String,
            destination: File
        ) = OneTimeWorkRequestBuilder<AppUpdateWorker>()
            .setConstraints(UpdateConstraints)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag(versionCode.toString())
            .setInputData(createAppUpdateWorkerParams(url, destination))
            .build()
    }
}