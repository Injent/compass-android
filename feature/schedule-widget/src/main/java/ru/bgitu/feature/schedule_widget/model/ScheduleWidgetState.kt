package ru.bgitu.feature.schedule_widget.model

import android.content.Context
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.feature.schedule_widget.R
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetDataPb
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetLessonPb

data class ScheduleWidgetState(
    val queryDate: LocalDate = DateTimeUtil.currentDate,
    val isLoading: Boolean = true,
    val options: WidgetOptions = WidgetOptions(),
    val classes: List<MinifiedLesson> = emptyList(),
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

    val classesForQueryDate: List<MinifiedLesson>
        get() = classes.filter { it.date == queryDate }

    companion object {
        fun defaultProtoInstance(): WidgetDataPb {
            return WidgetDataPb.newBuilder()
                .setQueryDate(DateTimeUtil.currentDate.toString())
                .setIsLoading(true)
                .setOptions(WidgetOptions().toProtoModel())
                .setWidgetVersion(1)
                .build()
        }
    }
}

fun WidgetDataPb.toWidgetState(): ScheduleWidgetState {
    return ScheduleWidgetState(
        queryDate = queryDate.toLocalDate(),
        isLoading = isLoading,
        options = WidgetOptions(
            opacity = options.opacity,
            widgetTheme = WidgetTheme.valueOf(options.theme),
            textColor = WidgetTextColor.valueOf(options.textColor),
            elementsColor = WidgetTextColor.valueOf(options.elementsColor),
        ),
        classes = lessonsList.map(WidgetLessonPb::toMinifiedLesson)
    )
}