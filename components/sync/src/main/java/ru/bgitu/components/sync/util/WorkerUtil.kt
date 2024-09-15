package ru.bgitu.components.sync.util

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import ru.bgitu.components.sync.R
import ru.bgitu.core.common.CommonDrawables
import ru.bgitu.core.common.DEFAULT_WORK_RETRY_ATTEMPTS
import ru.bgitu.core.notifications.Notifier
import ru.bgitu.core.notifications.channels.AppChannelManager

val SyncManagerConstraints: Constraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

fun Notification.toForegroundInfo(
    notificationId: Int = Notifier.BACKGROUND_NOTIFICATION_ID
): ForegroundInfo {
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

internal fun Context.syncNotification(): Notification {
    return NotificationCompat.Builder(this, AppChannelManager.BACKGROUND_WORK_CHANNEL_ID)
        .setSmallIcon(CommonDrawables.ic_compass_arrow)
        .setContentTitle(getString(R.string.sync_notification_title))
        .setContentText(getString(R.string.sync_notification_description))
        .setSilent(true)
        .setSound(null)
        .setOnlyAlertOnce(true)
        .setVisibility(NotificationCompat.VISIBILITY_SECRET)
        .build()
}