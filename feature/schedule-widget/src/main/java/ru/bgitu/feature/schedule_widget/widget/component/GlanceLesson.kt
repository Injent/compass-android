package ru.bgitu.feature.schedule_widget.widget.component

import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.AndroidRemoteViews
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.LessonDataUtils
import ru.bgitu.core.datastore.model.StoredLesson
import ru.bgitu.feature.schedule_widget.R
import ru.bgitu.feature.schedule_widget.model.WidgetColorScheme

@Composable
fun GlanceLesson(
    lesson: StoredLesson,
    opacity: Float,
    @DrawableRes background: Int,
    colorScheme: WidgetColorScheme,
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current

    AndroidRemoteViews(
        remoteViews = RemoteViews(context.packageName, R.layout.widget_item).apply {
            // Fill data
            setTextViewText(R.id.time_start, DateTimeUtil.formatTime(lesson.startAt))
            setTextViewText(R.id.time_end, DateTimeUtil.formatTime(lesson.endAt))

            setTextViewText(R.id.subject, lesson.subjectName)
            setTextViewText(
                R.id.location,
                LessonDataUtils.provideLocation(
                    context = context,
                    building = lesson.building,
                    classroom = lesson.classroom,
                    style = LessonDataUtils.DisplayStyle.SHORT
                )
            )

            // Set colors
            setImageViewResource(R.id.lesson_background_img, background)
            setInt(R.id.lesson_background_img, "setImageAlpha", (255 * opacity).toInt())

            setImageViewResource(
                R.id.lesson_icon,
                if (lesson.isLecture) R.drawable.ic_book else R.drawable.ic_flask
            )
            setInt(
                R.id.lesson_icon,
                "setColorFilter",
                colorScheme.brand.getColor(context).toArgb()
            )

            setTextColor(R.id.time_start, colorScheme.foreground2.getColor(context).toArgb())
            setTextColor(R.id.time_end, colorScheme.foreground2.getColor(context).toArgb())
            setTextColor(R.id.subject, colorScheme.foreground1.getColor(context).toArgb())
            setTextColor(R.id.location, colorScheme.foreground2.getColor(context).toArgb())

        },
        modifier = modifier
    )
}

