package ru.bgitu.feature.schedule_notifier.api

interface ScheduleNotifier {
    /**
     * @return `true` if alarm was successfully scheduled
     */
    fun enable(): Boolean
    fun disable(forToday: Boolean = false)
    fun restart()
}