package ru.bgitu.core.model.settings

import kotlinx.datetime.LocalDateTime

data class AppMetadata(
    val recentProfessorSearch: List<String>,
    val newestUpdateChecksum: String,
    val availableVersionCode: Long,
    val seenTeacherScheduleAlert: Boolean,
    val scheduleNotifierAlarmDateTime: LocalDateTime?,
    val isAnonymousUser: Boolean,
    val shouldShowMateBanner: Boolean,
    val shouldShowOnboarding: Boolean,
    val shouldShowDataResetAlert: Boolean,
    val unseenFeatureIds: List<Int>,
    val isPushMessagesInitialized: Boolean,
    val messagingToken: String
)