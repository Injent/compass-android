package ru.bgitu.feature.schedule_widget.model

import android.content.Context
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate
import ru.bgitu.core.SettingsPb
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.DateTimeUtil.isOddWeek
import ru.bgitu.core.datastore.model.StoredLesson
import ru.bgitu.core.datastore.model.StoredSchedule
import ru.bgitu.core.datastore.model.toStoredSchedule
import ru.bgitu.feature.schedule_widget.R

data class ScheduleWidgetState(
    val queryDate: LocalDate = DateTimeUtil.currentDate,
    val isLoading: Boolean = true,
    val options: WidgetOptions = WidgetOptions(),
    val schedule: StoredSchedule,
    val groupIsSelected: Boolean,
    val error: Boolean = false
) {
    fun getTitle(context: Context): String {
        val now = DateTimeUtil.currentDate

        return when {
            now == queryDate -> context.getString(R.string.today)
            now.plus(1, DateTimeUnit.DAY) == queryDate -> {
                context.getString(R.string.tomorrow)
            }
            now.minus(1, DateTimeUnit.DAY) == queryDate -> {
                context.getString(R.string.yesterday)
            }
            else -> DateTimeUtil.formatDay(queryDate)
        }
    }

    val currentLessons: List<StoredLesson>
        get() = if (queryDate.isOddWeek()) {
            schedule.firstWeek
        } else {
            schedule.secondWeek
        }.getOrDefault(queryDate.dayOfWeek, emptyList())
}

fun SettingsPb.toWidgetState(): ScheduleWidgetState {
    return ScheduleWidgetState(
        queryDate = runCatching { widgetState.queryDate.toLocalDate() }
            .getOrDefault(DateTimeUtil.currentDate),
        isLoading = widgetState.isLoading,
        schedule = schedule.toStoredSchedule(),
        options = WidgetOptions(
            opacity = widgetState.options.opacity,
            themeMode = WidgetThemeMode.entries[widgetState.options.mode]
        ),
        groupIsSelected = credentials.groupId != 0 && credentials.groupName.isNotEmpty()
    )
}