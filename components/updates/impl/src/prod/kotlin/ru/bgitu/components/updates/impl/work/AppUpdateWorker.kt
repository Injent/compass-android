package ru.bgitu.components.updates.impl.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import ru.bgitu.core.common.getOrElse
import ru.bgitu.core.common.runResulting
import ru.bgitu.core.data.downloader.AndroidFileDownloader
import ru.bgitu.core.data.downloader.DownloadState
import ru.bgitu.core.notifications.Notifier
import java.io.File
import kotlin.io.path.Path

class AppUpdateWorker(
    appContext: Context,
    private val params: WorkerParameters,
    private val ioDispatcher: CoroutineDispatcher,
    private val notifier: Notifier,
    private val fileDownloader: AndroidFileDownloader
) : CoroutineWorker(appContext, params), KoinComponent {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            Notifier.BACKGROUND_NOTIFICATION_ID,
            notifier.backgroundWorkNotification()
        )
    }

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        runResulting {
            val url = params.inputData.getString(PARAM_URL)!!
            val destination = Path(params.inputData.getString(PARAM_DESTINATION)!!)

            var workerResult: Result = Result.failure()
            fileDownloader.downloadAndSaveFile(url, destination.toFile())
                .transformWhile { state ->
                    emit(state)
                    state is DownloadState.Downloading
                }
                .onEach { state ->
                    setDownloadProgressAsync(state)

                    if (state is DownloadState.Finished) {
                        workerResult = Result.success()
                    }
                }
                .collect()

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