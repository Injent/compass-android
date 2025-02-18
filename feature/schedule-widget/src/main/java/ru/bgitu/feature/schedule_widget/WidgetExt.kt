package ru.bgitu.feature.schedule_widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.DpSize
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.glance.BitmapImageProvider
import androidx.glance.GlanceId
import androidx.glance.ImageProvider
import androidx.glance.appwidget.state.updateAppWidgetState
import ru.bgitu.core.WidgetStatePb
import ru.bgitu.core.copy
import ru.bgitu.core.designsystem.util.shadow.toPx
import ru.bgitu.feature.schedule_widget.model.WidgetOptions
import ru.bgitu.feature.schedule_widget.model.WidgetThemeMode
import ru.bgitu.feature.schedule_widget.widget.ScheduleStateDefinition
import ru.bgitu.feature.schedule_widget.widget.ScheduleWidget

fun Drawable.overrideColor(color: Color) {
    val argb = color.toArgb()
    when (this) {
        is GradientDrawable -> setColor(argb)
        is ShapeDrawable -> this.paint.color = argb
        is ColorDrawable -> this.color = argb
    }
}

private const val KEY_OPACITY = "opacity"
private const val KEY_THEME = "theme"

@Composable
internal fun rememberWidgetOptions(widgetOptions: WidgetOptions): MutableState<WidgetOptions> {
    return rememberSaveable(
        saver = Saver(
            save = {
                val options = it.value

                Bundle().apply {
                    putFloat(KEY_OPACITY, options.opacity)
                    putInt(KEY_THEME, options.themeMode.ordinal)
                }
            },
            restore = {
                mutableStateOf(
                    WidgetOptions(
                        opacity = it.getFloat(KEY_OPACITY),
                        themeMode = WidgetThemeMode.entries[it.getInt(KEY_THEME)]
                    )
                )
            }
        ),
        init = { mutableStateOf(widgetOptions) }
    )
}

suspend fun Context.updateScheduleWidgetState(
    glanceId: GlanceId,
    updateState: (WidgetStatePb) -> WidgetStatePb
) {
    updateAppWidgetState(
        context = this,
        definition = ScheduleStateDefinition,
        glanceId = glanceId,
        updateState = {
            it.copy {
                widgetState = updateState(widgetState)
            }
        }
    )
    ScheduleWidget().update(this, glanceId)
}

@SuppressLint("RestrictedApi")
fun createImageProvider(
    size: DpSize,
    cornerRadius: Int,
    color: Color,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1f
): ImageProvider {
    val radii = FloatArray(8) { cornerRadius.toFloat() }
    val shape = ShapeDrawable(RoundRectShape(radii, null, null))
    shape.paint.color = ColorUtils.setAlphaComponent(color.toArgb(), (255 * alpha).toInt())
    val bitmap = shape.toBitmap(width = size.width.value.toPx.toInt(), height = size.height.value.toPx.toInt())
    return BitmapImageProvider(bitmap)
}