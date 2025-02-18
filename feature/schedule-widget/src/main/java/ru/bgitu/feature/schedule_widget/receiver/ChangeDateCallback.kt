package ru.bgitu.feature.schedule_widget.receiver

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import org.koin.core.component.KoinComponent
import ru.bgitu.core.copy
import ru.bgitu.feature.schedule_widget.updateScheduleWidgetState

internal val dateKey = ActionParameters.Key<String>("date")

class ChangeDateCallback : ActionCallback, KoinComponent {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        context.updateScheduleWidgetState(
            glanceId = glanceId,
            updateState = {
                it.copy {
                    queryDate = requireNotNull(parameters[dateKey])
                    error = false
                    isLoading = false
                }
            }
        )
    }
}