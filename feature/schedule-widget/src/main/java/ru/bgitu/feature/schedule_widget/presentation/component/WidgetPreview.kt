package ru.bgitu.feature.schedule_widget.presentation.component

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ru.bgitu.feature.schedule_widget.R
import ru.bgitu.feature.schedule_widget.model.WidgetColors
import ru.bgitu.feature.schedule_widget.model.WidgetOptions
import ru.bgitu.feature.schedule_widget.model.WidgetTextColor
import ru.bgitu.feature.schedule_widget.overrideColor
import ru.bgitu.feature.schedule_widget.presentation.getViewsByType
import kotlin.math.roundToInt

@SuppressLint("InflateParams")
@Composable
fun WidgetPreview(
    options: WidgetOptions,
    colors: WidgetColors,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .widthIn(min = 300.dp)
            .heightIn(min = 240.dp, max = 290.dp)
    ) {
        AndroidView(
            factory = { context ->
                LayoutInflater.from(context)
                    .inflate(R.layout.schedule_widget_preview, null)
            },
            modifier = Modifier
                .matchParentSize()
                .padding(24.dp)
                .align(Alignment.Center)
        ) { view ->
            val widgetBackgroundView = view.findViewById<View>(R.id.widget_background)
            val widgetContainerView = view.findViewById<View>(R.id.widget_container)

            widgetBackgroundView.background.apply {
                alpha = (options.opacity * 255).roundToInt()

                overrideColor(colors.background)
            }
            widgetContainerView.background.apply {
                alpha = (options.opacity * 255).roundToInt()

                overrideColor(colors.container)
            }

            val onBackgroundColor = if (options.elementsColor == WidgetTextColor.FROM_THEME) {
                colors.onBackground.toArgb()
            } else context.getColor(options.elementsColor.colorResId)

            val onContainerColor = if (options.textColor == WidgetTextColor.FROM_THEME) {
                colors.onContainer.toArgb()
            } else context.getColor(options.textColor.colorResId)

            arrayOf(
                view.findViewById<ImageView>(R.id.btn_previous),
                view.findViewById(R.id.btn_next),
            ).forEach { it.setColorFilter(onBackgroundColor) }

            (view as ViewGroup).getViewsByType(TextView::class.java)
                .forEach {
                    it.setTextColor(onContainerColor)
                }

            view.findViewById<TextView>(R.id.btn_today).setTextColor(onBackgroundColor)
        }
    }
}