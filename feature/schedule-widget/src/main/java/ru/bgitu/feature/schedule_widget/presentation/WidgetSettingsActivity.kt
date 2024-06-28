package ru.bgitu.feature.schedule_widget.presentation

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.feature.schedule_widget.copy
import ru.bgitu.feature.schedule_widget.model.WidgetOptions
import ru.bgitu.feature.schedule_widget.model.toProtoModel
import ru.bgitu.feature.schedule_widget.model.toWidgetState
import ru.bgitu.feature.schedule_widget.widget.ScheduleStateDefinition
import ru.bgitu.feature.schedule_widget.widget.ScheduleWidget

class WidgetSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        actionBar?.hide()
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState)

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        var options by mutableStateOf(WidgetOptions())
        lifecycleScope.launch {
            options = ScheduleStateDefinition.getDataStore(applicationContext, "ignore")
                .data.first().toWidgetState().options
        }

        setContent {
            CompassTheme(isTranslucent = true) {
                WidgetSettingsScreen(
                    initialState = options,
                    onSave = { widgetOptions ->
                        lifecycleScope.launch {
                            updateAppWidgetState(
                                context = applicationContext,
                                definition = ScheduleStateDefinition,
                                glanceId = GlanceAppWidgetManager(applicationContext).getGlanceIdBy(appWidgetId),
                                updateState = {
                                    it.copy {
                                        this.options = widgetOptions.toProtoModel()
                                    }
                                }
                            )
                            ScheduleWidget().updateAll(applicationContext)
                            finishSettings(appWidgetId, true)
                        }
                    },
                    onCancel = {
                        finishSettings(appWidgetId, false)
                    }
                )

                val navigationBarColor = AppTheme.colorScheme.background1
                SideEffect {
                    window.navigationBarColor = navigationBarColor.toArgb()
                    window.statusBarColor = navigationBarColor.toArgb()
                }
            }
        }
    }

    private fun finishSettings(appWidgetId: Int, save: Boolean) {
        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(if (save) Activity.RESULT_OK else Activity.RESULT_CANCELED, resultValue)
        finish()
    }
}