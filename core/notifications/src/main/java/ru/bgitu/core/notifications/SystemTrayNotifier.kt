package ru.bgitu.core.notifications

import android.app.Notification
import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.bgitu.core.common.CommonDrawables
import ru.bgitu.core.notifications.channels.AppChannelManager

private const val MAX_NUM_NOTIFICATIONS = 5

class SystemTrayNotifier internal constructor(
    private val context: Context
) : Notifier {

    private val notificationManager by lazy { NotificationManagerCompat.from(context) }

    override fun backgroundWorkNotification(): Notification {
        return NotificationCompat.Builder(context, AppChannelManager.BACKGROUND_WORK_CHANNEL_ID)
            .setSmallIcon(CommonDrawables.ic_bgitu_notification)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setSilent(true)
            .setColor(Color.TRANSPARENT)
            .setShowWhen(false)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_DEFERRED)
            .setAllowSystemGeneratedContextualActions(false)
            .setLocalOnly(true)
            .build()
    }

    override fun cancel(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    override fun isNotificationVisible(notificationId: Int): Boolean {
        return notificationManager.activeNotifications
            .any {
                it.id == notificationId
            }
    }
}