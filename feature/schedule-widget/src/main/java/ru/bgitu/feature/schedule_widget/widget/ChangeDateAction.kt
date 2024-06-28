package ru.bgitu.feature.schedule_widget.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.data.repository.ScheduleRepository
import ru.bgitu.core.model.Lesson
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetDataPb
import ru.bgitu.feature.schedule_widget.copy
import ru.bgitu.feature.schedule_widget.model.toWidgetLesson

private const val LESSONS_LIMIT_OPTIMIZATION = 16

class ChangeDateAction : ActionCallback, KoinComponent {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val queryDateParam = parameters[QueryDateParam]
            ?: throw RuntimeException()
        val queryDate = queryDateParam.toLocalDate()

        val shouldLoad = queryDate in DateTimeUtil.weeksDateBoundary

        if (!shouldLoad) return

        context.updateWidgetState(glanceId = glanceId) {
            it.copy {
                this.isLoading = true
                this.queryDate = queryDateParam
            }
        }
        val from = queryDate.minus(1, DateTimeUnit.DAY)
        val to = queryDate.plus(1, DateTimeUnit.DAY)

        val lessons = get<ScheduleRepository>().getClasses(from, to, limit = LESSONS_LIMIT_OPTIMIZATION)
        context.updateWidgetState(glanceId = glanceId) {
            it.copy {
                this.isLoading = false
                this.lessons.clear()
                this.lessons.addAll(lessons.map(Lesson::toWidgetLesson))
                this.queryDate = queryDateParam
            }
        }
    }

    private suspend fun Context.updateWidgetState(
        glanceId: GlanceId,
        updateState: (WidgetDataPb) -> WidgetDataPb
    ) {
        updateAppWidgetState(
            context = this,
            definition = ScheduleStateDefinition,
            glanceId = glanceId,
            updateState = updateState
        )
        ScheduleWidget().update(this, glanceId)
    }

    companion object {
        internal val QueryDateParam = ActionParameters.Key<String>("change_date")
    }
}