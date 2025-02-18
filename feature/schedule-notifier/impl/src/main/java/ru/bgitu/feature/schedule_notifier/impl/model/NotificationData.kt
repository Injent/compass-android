package ru.bgitu.feature.schedule_notifier.impl.model

import ru.bgitu.core.datastore.model.StoredLesson

data class NotificationData(
    val lesson: StoredLesson?,
    val nextLesson: StoredLesson?,
    val type: MsgType,
)

enum class MsgType {
    START_SOON,
    ONGOING,
    NEXT_DAY,
    BREAK_STARTED
}