package ru.bgitu.feature.schedule_notifier.impl.model

import ru.bgitu.core.model.Lesson

data class NotificationData(
    val lesson: Lesson?,
    val nextLesson: Lesson?,
    val type: MsgType,
)

enum class MsgType {
    START_SOON,
    ONGOING,
    NEXT_DAY,
    BREAK_STARTED
}