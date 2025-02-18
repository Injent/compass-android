package ru.bgitu.core.notifications.channels

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import ru.bgitu.core.notifications.R

class AppChannelManager(private val context: Context) {
    private val notificationManager = NotificationManagerCompat.from(context)

    fun createChannels() {
        if (notificationManager.getNotificationChannel(GENERAL_CHANNEL_ID) != null) return

        createGeneralChannel()
        createBackgroundWorkChannel()
        createPinnedScheduleChannel()
    }

    private fun createGeneralChannel(): NotificationChannelCompat {
        val channel = NotificationChannelCompat.Builder(
            GENERAL_CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
        )
            .setName(context.getString(R.string.general_channel_name))
            .setShowBadge(true)
            .setLightsEnabled(true)
            .build()

        notificationManager.deleteNotificationChannel(GENERAL_CHANNEL_ID)
        notificationManager.createNotificationChannel(channel)
        return channel
    }

    private fun createBackgroundWorkChannel(): NotificationChannelCompat {
        val channel = NotificationChannelCompat.Builder(
            BACKGROUND_WORK_CHANNEL_ID,
            NotificationManager.IMPORTANCE_LOW
        )
            .setName(context.getString(R.string.background_work_channel_name))
            .setShowBadge(false)
            .setLightsEnabled(false)
            .setVibrationEnabled(false)
            .build()

        notificationManager.deleteNotificationChannel(BACKGROUND_WORK_CHANNEL_ID)
        notificationManager.createNotificationChannel(channel)
        return channel
    }

    private fun createPinnedScheduleChannel(): NotificationChannelCompat {
        val channel = NotificationChannelCompat.Builder(
            PINNED_SCHEDULE_CHANNEL_ID,
            NotificationManager.IMPORTANCE_DEFAULT
        )
            .setName(context.getString(R.string.pinned_schedule_channel_name))
            .setShowBadge(false)
            .setLightsEnabled(false)
            .setVibrationEnabled(false)
            .setSound(null, null)
            .build()

        notificationManager.deleteNotificationChannel(PINNED_SCHEDULE_CHANNEL_ID)
        notificationManager.createNotificationChannel(channel)
        return channel
    }

    companion object {
        const val GENERAL_CHANNEL_ID = "general_channel"
        const val BACKGROUND_WORK_CHANNEL_ID = "background_work_channel"
        const val PINNED_SCHEDULE_CHANNEL_ID = "pinned_schedule_channel"
    }
}