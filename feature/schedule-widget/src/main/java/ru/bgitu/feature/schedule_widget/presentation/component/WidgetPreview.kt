package ru.bgitu.feature.schedule_widget.presentation.component

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
import ru.bgitu.feature.schedule_widget.model.WidgetColorScheme
import ru.bgitu.feature.schedule_widget.model.WidgetOptions
import ru.bgitu.feature.schedule_widget.overrideColor
import ru.bgitu.feature.schedule_widget.presentation.getViewsByType

@SuppressLint("InflateParams")
@Composable
fun WidgetPreview(
    options: WidgetOptions,
    colorScheme: WidgetColorScheme,
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
                    .inflate(R.layout.widget_preview, null)
            },
            modifier = Modifier
                .matchParentSize()
                .padding(24.dp)
                .align(Alignment.Center)
        ) { view ->
            // Must be in first position of block function
            (view as ViewGroup).getViewsByType(TextView::class.java)
                .forEach { textView ->
                    textView.setTextColor(colorScheme.foreground2.getColor(context).toArgb())
                }

            view.findViewById<View>(R.id.widget_background).background.apply {
                overrideColor(colorScheme.widgetBackground.getColor(context))
                alpha = (options.opacity * 255).toInt()
            }

            arrayOf(
                view.findViewById<ImageButton>(R.id.previous_btn),
                view.findViewById(R.id.next_btn)
            ).forEach { button ->
                button.background.overrideColor(colorScheme.brand.getColor(context))
                button.setColorFilter(colorScheme.onBrand.getColor(context).toArgb())
            }

            arrayOf(
                view.findViewById<View>(R.id.item1),
                view.findViewById(R.id.item2),
                view.findViewById(R.id.item3),
            ).forEach { layout ->
                layout.background.overrideColor(colorScheme.background1.getColor(context))
                layout.background.alpha = (options.opacity * 255).toInt()
            }

            view.findViewById<TextView>(R.id.date_control).apply {
                background.overrideColor(colorScheme.background2.getColor(context))
                background.alpha = (options.opacity * 255).toInt()
                setTextColor(colorScheme.foreground1.getColor(context).toArgb())
            }

            arrayOf(
                view.findViewById<TextView>(R.id.subject1),
                view.findViewById(R.id.subject2),
                view.findViewById(R.id.subject3),
            ).forEach { textView ->
                textView.setTextColor(colorScheme.foreground1.getColor(context).toArgb())
            }

            arrayOf(
                view.findViewById<ImageView>(R.id.icon1),
                view.findViewById(R.id.icon2),
                view.findViewById(R.id.icon3),
            ).forEach { imageView ->
                imageView.setColorFilter(colorScheme.brand.getColor(context).toArgb())
            }
        }
    }
}