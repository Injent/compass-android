package ru.bgitu.components.sync.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.bgitu.components.sync.util.toForegroundInfo
import ru.bgitu.core.common.Result
import ru.bgitu.core.data.model.CloudMessagingTokenType
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.notifications.Notifier
import kotlin.time.Duration.Companion.hours
import kotlin.time.toJavaDuration

class RefreshTokenWorker(
    appContext: Context,
    params: WorkerParameters,
    private val settingsRepository: SettingsRepository,
    private val notifier: Notifier,
    private val ioDispatcher: CoroutineDispatcher
): CoroutineWorker(appContext, params) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return notifier.backgroundWorkNotification().toForegroundInfo()
    }

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        if (settingsRepository.credentials.first() == null) return@withContext Result.failure()

        val token = inputData.getString(INPUT_TOKEN)
        val type = inputData.getString(INPUT_TOKEN_TYPE)

        if (token == null || type == null)
            return@withContext Result.failure()


//        val success = authenticator.refreshCmt(
//            token = token,
//            type = CloudMessagingTokenType.valueOf(type)
//        ) is ru.bgitu.core.common.Result.Success
        Result.success()
    }

    companion object {
        private const val INPUT_TOKEN = "token"
        private const val INPUT_TOKEN_TYPE = "token_type"

        fun start(token: String, type: CloudMessagingTokenType): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<RefreshTokenWorker>(
                repeatInterval = 720.hours.toJavaDuration()
            )
                .setInputData(workDataOf(
                    INPUT_TOKEN to token,
                    INPUT_TOKEN_TYPE to type.name
                ))
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
        }
    }
}