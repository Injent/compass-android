package ru.bgitu.feature.schedule_widget.widget

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
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
import androidx.glance.LocalSize
import androidx.glance.action.Action
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.components.SquareIconButton
import androidx.glance.appwidget.lazy.LazyColumn
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
import ru.bgitu.feature.schedule_widget.model.MinifiedLesson
import ru.bgitu.feature.schedule_widget.model.ScheduleWidgetState
import ru.bgitu.feature.schedule_widget.model.WidgetColorScheme
import ru.bgitu.feature.schedule_widget.model.WidgetThemeMode
import ru.bgitu.feature.schedule_widget.model.toWidgetState
import ru.bgitu.feature.schedule_widget.widget.ChangeDateAction.Companion.QueryDateParam
import ru.bgitu.feature.schedule_widget.widget.component.CustomScaffold
import ru.bgitu.feature.schedule_widget.widget.component.GlanceLesson
import ru.bgitu.feature.schedule_widget.work.WidgetScheduleWorker


class ScheduleWidget : GlanceAppWidget() {

    private lateinit var colorScheme: WidgetColorScheme
    override val stateDefinition = ScheduleStateDefinition

    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val uiState = currentState<WidgetDataPb>().toWidgetState()
            colorScheme = WidgetColorScheme.createFrom(
                context = context,
                mode = uiState.options.themeMode,
                opacity = uiState.options.opacity
            )
            NewContent()
        }
    }

    @Composable
    fun NewContent() {
        val uiState: ScheduleWidgetState = currentState<WidgetDataPb>().toWidgetState()
        val context = LocalContext.current
        val size = LocalSize.current

        CustomScaffold(
            background = ImageProvider(
                when (uiState.options.themeMode) {
                    WidgetThemeMode.AUTO -> R.drawable.dynamic_widget_background
                    WidgetThemeMode.LIGHT -> R.drawable.light_widget_background
                    WidgetThemeMode.DARK -> R.drawable.dark_widget_background
                }
            ),
            horizontalPadding = if (size.width >= HORIZONTAL_RECTANGLE.width) 12.dp else 6.dp,
            titleBar = {
                TitleBar(
                    title = uiState.getTitle(context),
                    hasNext = uiState.queryDate > DateTimeUtil.weeksDateBoundary.start,
                    hasPrevious = uiState.queryDate < DateTimeUtil.weeksDateBoundary.endInclusive,
                    onNext = actionRunCallback<ChangeDateAction>(
                        actionParametersOf(
                            QueryDateParam to uiState.queryDate.plus(1, DateTimeUnit.DAY).toString()
                        )
                    ),
                    onPrevious = actionRunCallback<ChangeDateAction>(
                        actionParametersOf(
                            QueryDateParam to uiState.queryDate.minus(1, DateTimeUnit.DAY).toString()
                        )
                    ),
                )
            }
        ) {
            when {
                uiState.classesForQueryDate.isNotEmpty() -> {
                    DaySchedule(
                        lessons = uiState.classesForQueryDate,
                        themeMode = uiState.options.themeMode,
                        modifier = GlanceModifier
                    )
                }
                uiState.isLoading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = GlanceModifier
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            color = colorScheme.brand,
                            modifier = GlanceModifier
                                .size(48.dp),
                        )
                    }
                }
                uiState.classesForQueryDate.isEmpty() -> {
                    Column(
                        modifier = GlanceModifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            provider = ImageProvider(AppIllustrations.Happy),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(colorScheme.brand),
                            modifier = GlanceModifier.size(64.dp)
                        )
                        Spacer(GlanceModifier.height(8.dp))
                        Text(
                            text = LocalContext.current.getString(R.string.no_classes),
                            style = TextStyle(
                                color = colorScheme.brand,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun TitleBar(
        title: String,
        hasNext: Boolean,
        hasPrevious: Boolean,
        onNext: Action,
        onPrevious: Action,
    ) {
        val size = LocalSize.current

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(
                    horizontal = if (size.width >= HORIZONTAL_RECTANGLE.width) 12.dp else 8.dp,
                    vertical = if (size.width >= HORIZONTAL_RECTANGLE.width) 12.dp else 6.dp
                )
        ) {
            SquareIconButton(
                imageProvider = ImageProvider(R.drawable.ic_previous),
                backgroundColor = colorScheme.brand,
                contentColor = colorScheme.onBrand,
                contentDescription = null,
                enabled = hasPrevious,
                onClick = onPrevious,
                modifier = GlanceModifier.size(40.dp)
            )
            Spacer(GlanceModifier.width(AppTheme.spacing.s))
            Button(
                text = title,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = if (size.width <= SMALL_SQUARE.width) 13.sp else 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorScheme.background2,
                    contentColor = colorScheme.foreground1
                ),
                modifier = GlanceModifier
                    .padding(
                        top = if (size.width <= SMALL_SQUARE.width) 3.dp else 0.dp
                    )
                    .height(40.dp)
                    .defaultWeight(),
                onClick = actionRunCallback<ChangeDateAction>(
                    actionParametersOf(
                        QueryDateParam to DateTimeUtil.currentDate.toString()
                    )
                ),
                maxLines = 1
            )
            Spacer(GlanceModifier.width(AppTheme.spacing.s))
            SquareIconButton(
                imageProvider = ImageProvider(R.drawable.ic_next),
                backgroundColor = colorScheme.brand,
                contentColor = colorScheme.onBrand,
                contentDescription = null,
                enabled = hasNext,
                onClick = onNext,
                modifier = GlanceModifier.size(40.dp)
            )
        }
    }

    @Composable
    private fun DaySchedule(
        modifier: GlanceModifier = GlanceModifier,
        themeMode: WidgetThemeMode,
        lessons: List<MinifiedLesson>,
    ) {
        val context = LocalContext.current
        val action = actionStartActivity(
            Intent().apply {
                component = ComponentName(context, MAIN_ACTIVITY_CLASS)
                flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK
                        or Intent.FLAG_ACTIVITY_TASK_ON_HOME)
                val openScheduleDate = lessons.firstOrNull()?.date ?: DateTimeUtil.currentDate
                data = "$HOME_DEEPLINK/$openScheduleDate".toUri()
            }
        )
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxSize()
        ) {
            lessons.forEachIndexed { index, lesson ->
                item {
                    GlanceLesson(
                        lesson = lesson,
                        background = ImageProvider(
                            context.pickSurfaceDrawableId(
                                themeMode = themeMode,
                                index = index,
                                size = lessons.size
                            )
                        ),
                        iconColor = colorScheme.brand,
                        primaryContentColor = colorScheme.foreground1,
                        secondaryContentColor = colorScheme.foreground2,
                        action = action
                    )
                }

                item {
                    Spacer(GlanceModifier.height(4.dp))
                }
            }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        ScheduleStateDefinition.deleteDataStore(context)
        WidgetScheduleWorker.cancel(context)
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        super.onCompositionError(context, glanceId, appWidgetId, throwable)
        WidgetScheduleWorker.start(context, DateTimeUtil.currentDate)
    }

    companion object {
        private val SMALL_SQUARE = DpSize(250.dp, 250.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(320.dp, 100.dp)
    }
}

@SuppressLint("DiscouragedApi")
fun Context.pickSurfaceDrawableId(
    themeMode: WidgetThemeMode,
    index: Int,
    size: Int
): Int {
    val prefix = when (themeMode) {
        WidgetThemeMode.AUTO -> "dynamic"
        WidgetThemeMode.LIGHT -> "light"
        WidgetThemeMode.DARK -> "dark"
    }

    val postfix = when (index) {
        0 -> "_head"
        size - 1 -> "_tail"
        else -> ""
    }

    return resources.getIdentifier(
        "${prefix}_surface_shape$postfix",
        "drawable",
        packageName
    )
}