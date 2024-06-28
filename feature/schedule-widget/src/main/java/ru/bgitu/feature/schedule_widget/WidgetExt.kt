package ru.bgitu.feature.schedule_widget

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import ru.bgitu.feature.schedule_widget.model.WidgetOptions
import ru.bgitu.feature.schedule_widget.model.WidgetTextColor
import ru.bgitu.feature.schedule_widget.model.WidgetTheme

fun Drawable.overrideColor(color: Color) {
    val argb = color.toArgb()
    when (this) {
        is GradientDrawable -> setColor(argb)
        is ShapeDrawable -> this.paint.color = argb
        is ColorDrawable -> this.color = argb
    }
}

@Composable
internal fun GlanceModifier.compatBackground(
    color: Color,
    cornerRadius: Dp
): GlanceModifier {
    return if (Build.VERSION.SDK_INT >= 31) {
        this.background(color)
            .cornerRadius(cornerRadius)
    } else {
        this.background(color)
    }
}

private const val KEY_OPACITY = "opacity"
private const val KEY_THEME = "theme"
private const val KEY_TEXT_COLOR = "text_color"
private const val KEY_ELEMENTS_COLOR = "elements_color"

@Composable
internal fun rememberWidgetOptions(widgetOptions: WidgetOptions): MutableState<WidgetOptions> {
    return rememberSaveable(
        saver = Saver(
            save = {
                val options = it.value

                Bundle().apply {
                    putFloat(KEY_OPACITY, options.opacity)
                    putString(KEY_THEME, options.widgetTheme.name)
                    putString(KEY_TEXT_COLOR, options.textColor.name)
                    putString(KEY_ELEMENTS_COLOR, options.elementsColor.name)
                }
            },
            restore = {
                val options = WidgetOptions(
                    opacity = it.getFloat(KEY_OPACITY),
                    widgetTheme = WidgetTheme.valueOf(it.getString(KEY_THEME)!!),
                    textColor = WidgetTextColor.valueOf(it.getString(KEY_TEXT_COLOR)!!),
                    elementsColor = WidgetTextColor.valueOf(it.getString(KEY_ELEMENTS_COLOR)!!)
                )
                mutableStateOf(options)
            }
        ),
        key = "WIDGET_OPTIONS",
        init = { mutableStateOf(widgetOptions) }
    )
}