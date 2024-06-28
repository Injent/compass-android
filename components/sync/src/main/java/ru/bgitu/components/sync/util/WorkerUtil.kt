package ru.bgitu.components.sync.util

import android.app.Notification
import androidx.work.ForegroundInfo
import androidx.work.ListenableWorker
import ru.bgitu.core.common.DEFAULT_WORK_RETRY_ATTEMPTS

fun Notification.toForegroundInfo(notificationId: Int = 0): ForegroundInfo {
    return ForegroundInfo(notificationId, this)
}

fun ListenableWorker.successOrRetryUntil(
    success: Boolean,
    retryAttempts: Int = DEFAULT_WORK_RETRY_ATTEMPTS
): ListenableWorker.Result {
    return if (success) {
        ListenableWorker.Result.success()
    } else if (runAttemptCount <= retryAttempts) {
        ListenableWorker.Result.retry()
    } else ListenableWorker.Result.failure()
}