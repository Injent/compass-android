package ru.bgitu.feature.schedule_widget.widget.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.LessonDataUtils
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.feature.schedule_widget.model.MinifiedLesson

@Composable
fun GlanceLesson(
    lesson: MinifiedLesson,
    action: Action,
    background: ImageProvider,
    iconColor: ColorProvider,
    primaryContentColor: ColorProvider,
    secondaryContentColor: ColorProvider,
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(background)
            .clickable(onClick = action)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column {
            Spacer(GlanceModifier.height(3.dp))
            val timeTextStyle = TextStyle(
                color = secondaryContentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = DateTimeUtil.formatTime(lesson.startAt),
                style = timeTextStyle,
                maxLines = 1
            )
            Spacer(GlanceModifier.height(3.dp))
            Text(
                text = DateTimeUtil.formatTime(lesson.endAt),
                style = timeTextStyle,
                maxLines = 1
            )
        }
        Spacer(GlanceModifier.width(6.dp))
        Column(
            modifier = GlanceModifier.defaultWeight(),
        ) {
            Text(
                text = lesson.subject,
                style = TextStyle(
                    color = primaryContentColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 2,
                modifier = GlanceModifier.defaultWeight(),
            )
            val location = LessonDataUtils.provideLocation(
                context = context,
                building = lesson.building,
                classroom = lesson.classroom,
                style = LessonDataUtils.DisplayStyle.SHORT
            )
            Text(
                text = location,
                style = TextStyle(
                    color = secondaryContentColor,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal
                )
            )
        }
        Box(GlanceModifier.padding(top = 4.dp)) {
            Image(
                provider = ImageProvider(
                    if (lesson.isLecture) AppIcons.BookCover2 else AppIcons.Flask2
                ),
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconColor),
                modifier = GlanceModifier
                    .size(14.dp),
            )
        }
    }
}