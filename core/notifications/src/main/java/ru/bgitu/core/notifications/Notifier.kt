package ru.bgitu.core.notifications

import android.app.Notification

interface Notifier {
    fun backgroundWorkNotification(): Notification

    fun cancel(notificationId: Int)

    fun isNotificationVisible(notificationId: Int): Boolean

    companion object {
        const val BACKGROUND_NOTIFICATION_ID = 1
        const val PINNED_SCHEDULE_NOTIFICATION_ID = 874
    }
}