package ru.bgitu.feature.schedule_notifier.impl.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.LessonDataUtils
import ru.bgitu.core.common.atStartOfStudyDay
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.model.Lesson
import ru.bgitu.core.notifications.Notifier
import ru.bgitu.feature.schedule_notifier.api.ScheduleNotifier
import ru.bgitu.feature.schedule_notifier.impl.R
import ru.bgitu.feature.schedule_notifier.impl.ScheduleNotifierAlarm
import ru.bgitu.feature.schedule_notifier.impl.model.AlarmData
import ru.bgitu.feature.schedule_notifier.impl.model.ClassInNotification
import ru.bgitu.feature.schedule_notifier.impl.model.MsgType
import ru.bgitu.feature.schedule_notifier.impl.model.NotificationData
import ru.bgitu.feature.schedule_notifier.impl.notification.createScheduleNotification

class AlarmReceiver : BroadcastReceiver(), KoinComponent {
    private val _scheduleNotifier by inject<ScheduleNotifier>()
    private val scheduleRepository by inject<ScheduleRepository>()

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = NotificationManagerCompat.from(context)
        val scheduleNotifier = _scheduleNotifier as ScheduleNotifierAlarm

        val now = DateTimeUtil.currentDateTime
        val classes = runBlocking { scheduleRepository.getClasses(now.date) }
        val msg = initialMessage(
            now = now,
            classes = classes
        )
        val alarmData = context.getAlarmData(msg)

        if (alarmData.notification != null) {
            notificationManager.notify(
                Notifier.PINNED_SCHEDULE_NOTIFICATION_ID,
                alarmData.notification
            )
        } else {
            Toast.makeText(
                context,
                R.string.will_be_enabled_on_the_next_classroom,
                Toast.LENGTH_SHORT
            ).show()

            notificationManager.cancel(Notifier.PINNED_SCHEDULE_NOTIFICATION_ID)
        }

        scheduleNotifier.scheduleAlarmAt(
            triggerAt = alarmData.triggerAt,
            moved = alarmData.notification == null
        )
    }

    private fun initialMessage(
        now: LocalDateTime,
        classes: List<Lesson>,
    ): NotificationData {
        if (classes.isEmpty()) {
            return NotificationData(
                lesson = null,
                nextLesson = null,
                type = MsgType.NEXT_DAY
            )
        }
        val sortedClasses = classes.sortedBy { it.startAt }

        if (now >= sortedClasses.maxBy { it.endDateTime }.endDateTime) {
            return NotificationData(
                lesson = sortedClasses.first(),
                nextLesson = sortedClasses.getOrNull(1),
                type = MsgType.NEXT_DAY,
            )
        }

        if (sortedClasses.first().startDateTime > now) {
            return NotificationData(
                lesson = sortedClasses.first(),
                nextLesson = sortedClasses.getOrNull(1),
                type = MsgType.START_SOON,
            )
        }

        val ongoingClass = sortedClasses.find {
            it.startDateTime <= now && it.endDateTime > now
        }
        if (ongoingClass != null) {
            return NotificationData(
                lesson = ongoingClass,
                nextLesson = sortedClasses.getOrNull(sortedClasses.indexOf(ongoingClass) + 1),
                type = MsgType.ONGOING,
            )
        }

        for (i in sortedClasses.indices) {
            val lesson = sortedClasses[i]
            val nextLesson = sortedClasses.getOrNull(i + 1)

            if (lesson.endDateTime < now && nextLesson?.let { it.startDateTime > now } == true) {
                return NotificationData(
                    lesson = lesson,
                    nextLesson = nextLesson,
                    type = MsgType.BREAK_STARTED,
                )
            }

            if (i == sortedClasses.size - 1) {
                return NotificationData(
                    lesson = lesson,
                    nextLesson = null,
                    type = MsgType.BREAK_STARTED,
                )
            }
        }

        return NotificationData(
            lesson = sortedClasses.first(),
            nextLesson = null,
            type = MsgType.NEXT_DAY,
        )
    }

    private fun Context.getAlarmData(msg: NotificationData): AlarmData {
        return when (msg.type) {
            MsgType.START_SOON -> {
                val lesson = requireNotNull(msg.lesson)

                AlarmData(
                    triggerAt = lesson.startDateTime,
                    notification = createScheduleNotification(
                        currentClass = ClassInNotification(
                            subject = lesson.subject.name,
                            location = formatLocation(lesson),
                            startTime = DateTimeUtil.formatTime(lesson.startAt),
                            endTime = DateTimeUtil.formatTime(lesson.endAt),
                            shortMessage = shortLocationMessage(R.string.starts_soon, lesson),
                            startsSoon = true
                        ),
                        nextClass = msg.nextLesson?.let { nextLesson ->
                            ClassInNotification(
                                subject = nextLesson.subject.name,
                                location = formatLocation(nextLesson),
                                startTime = DateTimeUtil.formatTime(nextLesson.startAt),
                                endTime = DateTimeUtil.formatTime(nextLesson.endAt),
                                shortMessage = shortLocationMessage(R.string.next, nextLesson),
                                startsSoon = true
                            )
                        } ?: ClassInNotification.Empty.copy(
                            shortMessage = "${getString(R.string.next)} ${getString(R.string.then_end_of_classes)}",
                            startTime = DateTimeUtil.formatTime(lesson.endAt)
                        ),
                    )
                )
            }
            MsgType.ONGOING -> {
                val lesson = requireNotNull(msg.lesson)
                AlarmData(
                    triggerAt = lesson.endDateTime,
                    notification = createScheduleNotification(
                        currentClass = ClassInNotification(
                            subject = lesson.subject.name,
                            location = formatLocation(lesson),
                            startTime = DateTimeUtil.formatTime(lesson.startAt),
                            endTime = DateTimeUtil.formatTime(lesson.endAt),
                            shortMessage = shortLocationMessage(R.string.now, lesson)
                        ),
                        nextClass = msg.nextLesson?.let { nextLesson ->
                            ClassInNotification(
                                subject = nextLesson.subject.name,
                                location = formatLocation(nextLesson),
                                startTime = DateTimeUtil.formatTime(nextLesson.startAt),
                                endTime = DateTimeUtil.formatTime(nextLesson.endAt),
                                shortMessage = shortLocationMessage(R.string.next, nextLesson)
                            )
                        } ?: ClassInNotification.Empty.copy(
                            shortMessage = "${getString(R.string.next)} ${getString(R.string.then_end_of_classes)}",
                            startTime = DateTimeUtil.formatTime(lesson.endAt)
                        ),
                    )
                )
            }
            MsgType.NEXT_DAY -> AlarmData(
                triggerAt = (msg.lesson?.date ?: DateTimeUtil.currentDate).atStartOfStudyDay(),
                notification = null
            )
            MsgType.BREAK_STARTED -> {
                AlarmData(
                    triggerAt = msg.nextLesson?.startDateTime
                        ?: requireNotNull(msg.lesson).date.atStartOfStudyDay(),
                    notification = createScheduleNotification(
                        currentClass = ClassInNotification(getString(R.string.break_title)),
                        nextClass = msg.nextLesson?.let { nextLesson ->
                            ClassInNotification(
                                subject = nextLesson.subject.name,
                                location = formatLocation(nextLesson),
                                startTime = DateTimeUtil.formatTime(nextLesson.startAt),
                                endTime = DateTimeUtil.formatTime(nextLesson.endAt),
                                shortMessage = shortLocationMessage(R.string.next, nextLesson)
                            )
                        } ?: ClassInNotification(
                            shortMessage = "${getString(R.string.next)} ${getString(R.string.then_end_of_classes)}"
                        ),
                    )
                )
            }
        }
    }

    private fun Context.shortLocationMessage(
        @StringRes statusResId: Int,
        lesson: Lesson
    ): String {
        val subject = LessonDataUtils.shortenSentence(lesson.subject.name)
        return "${getString(statusResId)}: ${formatLocation(lesson)}, $subject"
    }

    private fun Context.formatLocation(lesson: Lesson): String {
        return LessonDataUtils.provideLocation(
            context = this,
            building = lesson.building,
            classroom = lesson.classroom,
            style = LessonDataUtils.DisplayStyle.SHORT
        )
    }
}