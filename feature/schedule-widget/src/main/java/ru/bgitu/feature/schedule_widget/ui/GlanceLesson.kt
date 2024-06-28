package ru.bgitu.feature.schedule_widget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import ru.bgitu.core.common.CommonStrings
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.LessonDataUtils
import ru.bgitu.feature.schedule_widget.model.MinifiedLesson

@Composable
fun GlanceLesson(
    lesson: MinifiedLesson,
    contentColor: ColorProvider,
    maxLines: Int = 1,
    modifier: GlanceModifier = GlanceModifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val timeTextStyle = TextStyle(
                color = contentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = DateTimeUtil.formatTime(lesson.startAt),
                style = timeTextStyle,
                maxLines = maxLines
            )
            Spacer(GlanceModifier.height(4.dp))
            Text(
                text = DateTimeUtil.formatTime(lesson.endAt),
                style = timeTextStyle,
                maxLines = maxLines
            )
        }
        Spacer(GlanceModifier.width(12.dp))
        Column(
            modifier = GlanceModifier.defaultWeight()
        ) {
            val context = LocalContext.current
            Row(GlanceModifier.fillMaxWidth()) {
                Text(
                    text = lesson.subject,
                    style = TextStyle(
                        color = contentColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    modifier = GlanceModifier.defaultWeight(),
                )
                Text(
                    text = context.getString(
                        if (lesson.isLecture) {
                            CommonStrings.lecture_abbriviation
                        } else CommonStrings.practice_abbriviation
                    ),
                    style = TextStyle(
                        color = contentColor,
                        fontSize = 16.sp
                    )
                )
            }
            val location = LessonDataUtils.provideLocation(
                    context = context,
                    building = lesson.building,
                    classroom = lesson.classroom,
                    style = LessonDataUtils.DisplayStyle.SHORT
                )
            Text(
                text = location,
                style = TextStyle(
                    color = contentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}