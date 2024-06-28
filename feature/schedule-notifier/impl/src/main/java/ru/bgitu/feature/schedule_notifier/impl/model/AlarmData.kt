package ru.bgitu.feature.schedule_notifier.impl.model

import android.app.Notification
import kotlinx.datetime.LocalDateTime

data class AlarmData(
    val triggerAt: LocalDateTime,
    val notification: Notification?,
)
