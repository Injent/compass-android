package ru.bgitu.feature.schedule_notifier.impl.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.atStartOfStudyDay
import ru.bgitu.feature.schedule_notifier.api.ScheduleNotifier
import ru.bgitu.feature.schedule_notifier.impl.ScheduleNotifierAlarm

class TurnOffForTodayReceiver : BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context, intent: Intent) {
        val scheduleNotifier = get<ScheduleNotifier>() as ScheduleNotifierAlarm
        scheduleNotifier.disable(forToday = true)
    }
}