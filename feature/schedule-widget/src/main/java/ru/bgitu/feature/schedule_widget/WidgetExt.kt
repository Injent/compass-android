package ru.bgitu.feature.schedule_widget

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import ru.bgitu.feature.schedule_widget.model.WidgetOptions

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
                }
            },
            restore = {
                TODO()
            }
        ),
        key = "WIDGET_OPTIONS",
        init = { mutableStateOf(widgetOptions) }
    )
}