package ru.bgitu.core.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build.VERSION.SDK_INT
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import ru.bgitu.core.common.CommonDrawables
import ru.bgitu.core.common.MAIN_ACTIVITY_CLASS
import ru.bgitu.core.model.settings.SubscribedTopic
import ru.bgitu.core.notifications.channels.AppChannelManager

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

    @SuppressLint("MissingPermission")
    override fun showPushNotification(
        notificationId: Int,
        title: String,
        body: String,
        url: String?,
        topic: SubscribedTopic
    ) {
        val notification = NotificationCompat.Builder(context, AppChannelManager.GENERAL_CHANNEL_ID)
            .setSmallIcon(CommonDrawables.app_logo_monochrome)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setPriority(
                when (topic) {
                    SubscribedTopic.BROADCAST,
                    SubscribedTopic.SCHEDULE_CHANGE -> NotificationCompat.PRIORITY_DEFAULT
                    SubscribedTopic.SEVERE -> NotificationCompat.PRIORITY_MAX
                }
            )
            .apply {
                if (url == null) {
                    setContentIntent(
                        PendingIntent.getActivity(
                            context,
                            0,
                            Intent.makeMainActivity(ComponentName(context, MAIN_ACTIVITY_CLASS)),
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    )
                } else {
                    setContentIntent(
                        PendingIntent.getActivity(
                            context,
                            0,
                            Intent(Intent.ACTION_VIEW, url.toUri()),
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    )
                }
            }
            .setColorized(true)
            .setColor(
                when (topic) {
                    SubscribedTopic.BROADCAST -> Color.rgb(24, 205, 237)
                    SubscribedTopic.SCHEDULE_CHANGE -> Color.rgb(33, 163, 102)
                    SubscribedTopic.SEVERE -> Color.rgb(255, 69, 58)
                }
            )
            .setContentTitle(title)
            .setContentText(body)
            .build()

        if (
            (SDK_INT >= 33 && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED)
            || SDK_INT < 33
        ) {
            notificationManager.notify(notificationId, notification)
        }
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