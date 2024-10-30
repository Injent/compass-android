package ru.bgitu.feature.schedule_notifier.impl.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import ru.bgitu.core.common.CommonDrawables
import ru.bgitu.core.common.MAIN_ACTIVITY_CLASS
import ru.bgitu.core.notifications.channels.AppChannelManager
import ru.bgitu.feature.schedule_notifier.impl.R
import ru.bgitu.feature.schedule_notifier.impl.model.ClassInNotification
import ru.bgitu.feature.schedule_notifier.impl.receiver.TurnOffForTodayReceiver

fun Context.createScheduleNotification(
    currentClass: ClassInNotification,
    nextClass: ClassInNotification,
): Notification {
    println("CREATED NOTIFICATIN")

    return NotificationCompat.Builder(this, AppChannelManager.PINNED_SCHEDULE_CHANNEL_ID)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setOngoing(true)
        .setAutoCancel(false)
        .setSmallIcon(CommonDrawables.ic_compass_arrow)
        .setWhen(0L)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setSound(null)
        .setCustomContentView(
            createRemoteViews(
                title = currentClass.shortMessage,
                content = nextClass.shortMessage
            )
        )
        .setCustomBigContentView(
            createBigRemoteViews(currentClass, nextClass)
        )
        .setContentIntent(
            PendingIntent.getActivity(
                this,
                0,
                Intent.makeMainActivity(ComponentName(packageName, MAIN_ACTIVITY_CLASS)),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        .build()
}

private fun Context.createRemoteViews(
    title: String,
    content: String
): RemoteViews {
    val remoteViews = RemoteViews(packageName, R.layout.schedule_notification)
    remoteViews.setTextViewText(R.id.schedule_title, title)
    remoteViews.setTextViewText(R.id.schedule_content, content)
    return remoteViews
}

private fun Context.createBigRemoteViews(
    currentClass: ClassInNotification,
    nextClass: ClassInNotification
): RemoteViews {
    val remoteViews = RemoteViews(packageName, R.layout.schedule_notification_big)

    with(remoteViews) {
        setTextViewText(R.id.current_subject, currentClass.subject)
        setTextViewText(R.id.current_location, currentClass.location)
        setTextViewText(R.id.current_time_start, currentClass.startTime)
        setTextViewText(R.id.current_time_end, currentClass.endTime)

        if (currentClass.isEmpty()) {
            setTextViewText(R.id.current_subject, getString(R.string.break_title))
        }
        if (currentClass.startsSoon) {
            setTextViewText(R.id.now, getString(R.string.starts_soon))
        }

        if (nextClass.isEmpty()) {
            setTextViewText(R.id.next_subject, nextClass.shortMessage)
            setTextViewText(R.id.next_time_start, nextClass.startTime)
        } else {
            setTextViewText(R.id.next_subject, nextClass.subject)
            setTextViewText(R.id.next_location, nextClass.location)
            setTextViewText(R.id.next_time_start, nextClass.startTime)
            setTextViewText(R.id.next_time_end, nextClass.endTime)
        }
    }
    remoteViews.setOnClickPendingIntent(
        R.id.turn_off,
        PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, TurnOffForTodayReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    )
    return remoteViews
}