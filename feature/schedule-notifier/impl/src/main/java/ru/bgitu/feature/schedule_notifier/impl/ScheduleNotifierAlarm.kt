package ru.bgitu.feature.schedule_notifier.impl

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.notifications.Notifier
import ru.bgitu.feature.schedule_notifier.api.ScheduleNotifier
import ru.bgitu.feature.schedule_notifier.impl.receiver.AlarmReceiver
import java.time.ZoneId

class ScheduleNotifierAlarm(
    private val context: Context,
    private val settingsRepository: SettingsRepository
) : ScheduleNotifier {

    private val notificationManager by lazy { NotificationManagerCompat.from(context) }
    private val alarmManager = context.getSystemService<AlarmManager>()!!

    override fun enable(): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, R.string.notification_disallowed, Toast.LENGTH_SHORT).show()
            return false
        }

        if (SDK_INT >= 31 && !alarmManager.canScheduleExactAlarms()) {
            context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            Toast.makeText(context, R.string.alarm_disabled, Toast.LENGTH_SHORT).show()
            return false
        }
        context.sendBroadcast(Intent(context, AlarmReceiver::class.java))

        runBlocking {
            settingsRepository.updateUserPrefs {
                it.copy(showPinnedSchedule = true)
            }
        }

        return true
    }

    override fun disable(forToday: Boolean) {
        alarmManager.cancel(context.getAlarmPendingIntent())
        notificationManager.cancel(Notifier.PINNED_SCHEDULE_NOTIFICATION_ID)
        if (!forToday) {
            runBlocking {
                settingsRepository.updateUserPrefs {
                    it.copy(showPinnedSchedule = false)
                }
                settingsRepository.updateMetadata {
                    it.copy(scheduleNotifierAlarmDateTime = null)
                }
            }
        }
    }

    override fun restart() {
        if (notificationManager.areNotificationsEnabled().not()) return

        runBlocking {
            val wasEnabled = settingsRepository.data.first().userPrefs.showPinnedSchedule

            val isNotificationVisible = notificationManager.activeNotifications.any {
                it.id == Notifier.PINNED_SCHEDULE_NOTIFICATION_ID
            }

            val alarmDateTime = settingsRepository.metadata.first().scheduleNotifierAlarmDateTime
            if (wasEnabled && !isNotificationVisible &&
                (alarmDateTime == null || alarmDateTime < DateTimeUtil.currentDateTime)) {
                enable()
            }
        }
    }

    @SuppressLint("MissingPermission")
    internal fun scheduleAlarmAt(triggerAt: LocalDateTime, moved: Boolean = false) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC,
            triggerAt.toJavaLocalDateTime()
                .atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            context.getAlarmPendingIntent()
        )
        if (moved) {
            runBlocking {
                settingsRepository.updateMetadata {
                    it.copy(scheduleNotifierAlarmDateTime = triggerAt)
                }
            }
        }
    }

    private fun Context.getAlarmPendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
            this,
            0,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}