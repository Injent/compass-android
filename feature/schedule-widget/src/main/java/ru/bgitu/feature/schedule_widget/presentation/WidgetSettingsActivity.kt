package ru.bgitu.feature.schedule_widget.presentation

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.bgitu.core.copy
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.feature.schedule_widget.R
import ru.bgitu.feature.schedule_widget.model.ScheduleWidgetState
import ru.bgitu.feature.schedule_widget.model.toProtoModel
import ru.bgitu.feature.schedule_widget.model.toWidgetState
import ru.bgitu.feature.schedule_widget.updateScheduleWidgetState
import ru.bgitu.feature.schedule_widget.widget.ScheduleStateDefinition

class WidgetSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        if (SDK_INT in 29..34) {
            @Suppress("DEPRECATION")
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }
        super.onCreate(savedInstanceState)

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Toast.makeText(
                this,
                R.string.toast_failed_to_open_settings,
                Toast.LENGTH_LONG
            ).show()
            finish()
        }

        val options by mutableStateOf(runBlocking { getWidgetState().options })

        setContent {
            CompassTheme(isTranslucent = true) {
                WidgetSettingsScreen(
                    initialState = options,
                    onSave = { widgetOptions ->
                        lifecycleScope.launch {
                            updateScheduleWidgetState(
                                glanceId = GlanceAppWidgetManager(applicationContext).getGlanceIdBy(appWidgetId),
                                updateState = {
                                    it.copy {
                                        this.options = widgetOptions.toProtoModel()
                                    }
                                }
                            )
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

    private suspend fun getWidgetState(): ScheduleWidgetState {
        return ScheduleStateDefinition.getDataStore(applicationContext, "ignore")
            .data.first().toWidgetState()
    }
}