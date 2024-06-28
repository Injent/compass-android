package ru.bgitu.feature.schedule_widget.widget

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.Visibility
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.visibility
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.HOME_DEEPLINK
import ru.bgitu.core.common.MAIN_ACTIVITY_CLASS
import ru.bgitu.core.designsystem.icon.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.schedule_widget.R
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetDataPb
import ru.bgitu.feature.schedule_widget.compatBackground
import ru.bgitu.feature.schedule_widget.model.MinifiedLesson
import ru.bgitu.feature.schedule_widget.model.ScheduleWidgetState
import ru.bgitu.feature.schedule_widget.model.WidgetColors
import ru.bgitu.feature.schedule_widget.model.provideWidgetColors
import ru.bgitu.feature.schedule_widget.model.toWidgetState
import ru.bgitu.feature.schedule_widget.ui.GlanceLesson
import ru.bgitu.feature.schedule_widget.widget.ChangeDateAction.Companion.QueryDateParam
import ru.bgitu.feature.schedule_widget.work.ScheduleWorker

const val EXTRA_OPENED_SCHEDULE_DATE = "opened_schedule_date"

class ScheduleWidget : GlanceAppWidget() {

    private lateinit var colors: WidgetColors
    override val stateDefinition = ScheduleStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val uiState = currentState<WidgetDataPb>().toWidgetState()
            colors = provideWidgetColors(context, uiState.options)
            Content()
        }
    }

    @Composable
    private fun Content() {
        val uiState: ScheduleWidgetState = currentState<WidgetDataPb>().toWidgetState()
        val context = LocalContext.current

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.Top,
            modifier = GlanceModifier
                .fillMaxSize()
                .appWidgetBackground()
                .compatBackground(
                    color = colors.background.copy(alpha = uiState.options.opacity),
                    cornerRadius = 16.dp
                )
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        ) {
            val onBackgroundColor = ColorProvider(colors.onBackground)

            val reachStart = uiState.queryDate <= DateTimeUtil.weeksDateBoundary.start
            val reachEnd = uiState.queryDate >= DateTimeUtil.weeksDateBoundary.endInclusive
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    provider = ImageProvider(R.drawable.ic_back),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(onBackgroundColor),
                    modifier = GlanceModifier
                        .then(
                            if (reachStart) {
                                GlanceModifier.visibility(Visibility.Invisible)
                            } else {
                                val previousDate = uiState.queryDate.minus(1, DateTimeUnit.DAY)
                                val action = actionRunCallback<ChangeDateAction>(
                                    actionParametersOf(QueryDateParam to previousDate.toString())
                                )
                                GlanceModifier
                                    .clickable(action)
                            }
                        )
                        .padding(8.dp)
                )
                Spacer(GlanceModifier.width(AppTheme.spacing.s))
                Button(
                    text = uiState.getTitle(context),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ColorProvider(Color.Transparent),
                        contentColor = onBackgroundColor
                    ),
                    modifier = GlanceModifier
                        .height(48.dp)
                        .defaultWeight(),
                    onClick = actionRunCallback<ChangeDateAction>(
                        actionParametersOf(
                            QueryDateParam to DateTimeUtil.currentDate.toString()
                        )
                    ),
                    maxLines = 1
                )
                Spacer(GlanceModifier.width(AppTheme.spacing.s))
                Image(
                    provider = ImageProvider(R.drawable.ic_back_backwards),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(onBackgroundColor),
                    modifier = GlanceModifier
                        .then(
                            if (reachEnd) {
                                GlanceModifier
                                    .visibility(Visibility.Invisible)
                            } else {
                                val nextDate = uiState.queryDate.plus(1, DateTimeUnit.DAY)
                                val action = actionRunCallback<ChangeDateAction>(
                                    actionParametersOf(QueryDateParam to nextDate.toString())
                                )
                                GlanceModifier
                                    .clickable(action)
                            }
                        )
                        .padding(8.dp)
                )
            }
            val backgroundColor = colors.container.copy(alpha = uiState.options.opacity)
            val onContainerColor = ColorProvider(colors.onContainer)

            when {
                uiState.classesForQueryDate.isNotEmpty() -> {
                    DaySchedule(
                        lessons = uiState.classesForQueryDate,
                        backgroundColor = backgroundColor,
                        contentColor = onContainerColor
                    )
                }
                uiState.isLoading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .compatBackground(color = backgroundColor, cornerRadius = 12.dp),
                    ) {
                        CircularProgressIndicator(
                            color = onBackgroundColor,
                            modifier = GlanceModifier
                                .size(48.dp),
                        )
                    }
                }
                uiState.classesForQueryDate.isEmpty() -> {
                    Column(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .compatBackground(color = backgroundColor, cornerRadius = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            provider = ImageProvider(AppIllustrations.Happy),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(onContainerColor),
                            modifier = GlanceModifier.size(64.dp)
                        )
                        Spacer(GlanceModifier.height(8.dp))
                        Text(
                            text = LocalContext.current.getString(R.string.no_classes),
                            style = TextStyle(
                                color = onContainerColor,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun DaySchedule(
        lessons: List<MinifiedLesson>,
        backgroundColor: Color,
        contentColor: ColorProvider
    ) {
        val context = LocalContext.current
        val action = actionStartActivity(
            Intent().apply {
                setComponent(ComponentName(context, MAIN_ACTIVITY_CLASS))
                setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            or Intent.FLAG_ACTIVITY_TASK_ON_HOME
                )
                val openScheduleDate = lessons.firstOrNull()?.date ?: DateTimeUtil.currentDate
                data = "$HOME_DEEPLINK/$openScheduleDate".toUri()
            }
        )
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = GlanceModifier
                .fillMaxSize()
                .compatBackground(color = backgroundColor, cornerRadius = 12.dp),
        ) {
            val lastItemIndex = lessons.size - 1
            itemsIndexed(
                items = lessons,
                itemId = { index, _ -> index.toLong() }
            ) { index, lesson ->
                GlanceLesson(
                    lesson = lesson,
                    contentColor = contentColor,
                    maxLines = 1,
                    modifier = GlanceModifier
                        .clickable(action)
                        .padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        )
                        .then(
                            when (index) {
                                0 -> GlanceModifier.padding(top = 4.dp)
                                lastItemIndex -> GlanceModifier.padding(bottom = 4.dp)
                                else -> GlanceModifier
                            }
                        )
                )
            }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        ScheduleStateDefinition.deleteDataStore(context)
        ScheduleWorker.cancel(context)
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        super.onCompositionError(context, glanceId, appWidgetId, throwable)
        ScheduleWorker.start(context, DateTimeUtil.currentDate)
    }
}