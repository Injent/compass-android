package ru.bgitu.core.notifications

import android.app.Notification
import ru.bgitu.core.model.settings.SubscribedTopic

interface Notifier {
    fun backgroundWorkNotification(): Notification

    fun cancel(notificationId: Int)

    fun isNotificationVisible(notificationId: Int): Boolean

    fun showPushNotification(
        notificationId: Int,
        title: String,
        body: String,
        url: String?,
        topic: SubscribedTopic
    )

    companion object {
        const val BACKGROUND_NOTIFICATION_ID = 1
        const val PINNED_SCHEDULE_NOTIFICATION_ID = 874
    }
}