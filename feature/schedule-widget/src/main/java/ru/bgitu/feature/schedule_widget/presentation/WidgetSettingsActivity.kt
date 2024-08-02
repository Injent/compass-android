package ru.bgitu.feature.schedule_widget.presentation

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.View.LAYER_TYPE_SOFTWARE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.feature.schedule_widget.copy
import ru.bgitu.feature.schedule_widget.model.WidgetOptions
import ru.bgitu.feature.schedule_widget.model.toProtoModel
import ru.bgitu.feature.schedule_widget.model.toWidgetState
import ru.bgitu.feature.schedule_widget.widget.ScheduleStateDefinition
import ru.bgitu.feature.schedule_widget.widget.ScheduleWidget

class WidgetSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        if (SDK_INT >= 29) {
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }
        if (SDK_INT < 29) {
            window.decorView.rootView.setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
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
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        overrideLargeFontSize()
    }

    private fun overrideLargeFontSize() {
        Configuration().apply {
            setTo(baseContext.resources.configuration)
            fontScale = fontScale.coerceAtMost(1.5f)
            applyOverrideConfiguration(this)
        }
    }

    private fun finishSettings(appWidgetId: Int, save: Boolean) {
        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(if (save) RESULT_OK else RESULT_CANCELED, resultValue)
        finish()
    }
}