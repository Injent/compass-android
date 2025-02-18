package ru.bgitu.feature.schedule_notifier.api

interface ScheduleNotifier {
    /**
     * @return `true` if alarm was successfully scheduled
     */
    fun enable(): Boolean
    fun disable(forToday: Boolean = false)

    /**
     * @param forced set to true if you want force notification recreation
     */
    fun restart(forced: Boolean = false)
}