package ru.bgitu.core.model.settings

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

data class AppMetadata(
    val lastStatisticsReportDate: Instant,
    val appUuid: String,
    val recentProfessorSearch: List<String>,
    val newestUpdateChecksum: String,
    val availableVersionCode: Long,
    val seenTeacherScheduleAlert: Boolean,
    val scheduleNotifierAlarmDateTime: LocalDateTime? = null
)