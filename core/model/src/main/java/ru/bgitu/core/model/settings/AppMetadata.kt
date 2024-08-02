package ru.bgitu.core.model.settings

import kotlinx.datetime.LocalDateTime

data class AppMetadata(
    val recentProfessorSearch: List<String>,
    val newestUpdateChecksum: String,
    val availableVersionCode: Long,
    val seenTeacherScheduleAlert: Boolean,
    val scheduleNotifierAlarmDateTime: LocalDateTime? = null,
    val isAnonymousUser: Boolean,
    val isMateBannerClosed: Boolean
)