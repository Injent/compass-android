package ru.bgitu.feature.schedule_widget.widget

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
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
import androidx.glance.action.clickable
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
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import ru.bgitu.core.SettingsPb
import ru.bgitu.core.WidgetStatePb
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.HOME_DEEPLINK
import ru.bgitu.core.common.MAIN_ACTIVITY_CLASS
import ru.bgitu.core.copy
import ru.bgitu.core.designsystem.illustration.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.schedule_widget.R
import ru.bgitu.feature.schedule_widget.model.ScheduleWidgetState
import ru.bgitu.feature.schedule_widget.model.WidgetColorScheme
import ru.bgitu.feature.schedule_widget.model.WidgetThemeMode
import ru.bgitu.feature.schedule_widget.model.toWidgetState
import ru.bgitu.feature.schedule_widget.receiver.ChangeDateCallback
import ru.bgitu.feature.schedule_widget.receiver.dateKey
import ru.bgitu.feature.schedule_widget.updateScheduleWidgetState
import ru.bgitu.feature.schedule_widget.widget.component.CustomScaffold
import ru.bgitu.feature.schedule_widget.widget.component.GlanceLesson


class ScheduleWidget : GlanceAppWidget() {

    private lateinit var colorScheme: WidgetColorScheme
    override val stateDefinition = ScheduleStateDefinition

    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val uiState = currentState<SettingsPb>().toWidgetState()
            colorScheme = WidgetColorScheme.createFrom(
                context = context,
                mode = uiState.options.themeMode,
                opacity = uiState.options.opacity
            )
            NewContent(uiState = uiState)
        }
    }

    @SuppressLint("RestrictedApi")
    @Composable
    fun NewContent(uiState: ScheduleWidgetState) {
        val context = LocalContext.current
        val size = LocalSize.current

        CustomScaffold(
            backgroundColor = run {
                colorScheme.widgetBackground.getColor(context)
            },
            backgroundAlpha = uiState.options.opacity,
            horizontalPadding = when {
                SDK_INT < 31 -> 14.dp
                size.width >= HORIZONTAL_RECTANGLE.width -> 12.dp
                else -> 6.dp
            },
            titleBar = {
                TitleBar(
                    title = uiState.getTitle(context),
                    hasNext = (uiState.queryDate > DateTimeUtil.weeksDateBoundary.start)
                            && uiState.groupIsSelected,
                    hasPrevious = (uiState.queryDate < DateTimeUtil.weeksDateBoundary.endInclusive)
                            && uiState.groupIsSelected,
                    onNext = changeDateAction(uiState.queryDate.plus(1, DateTimeUnit.DAY)),
                    onPrevious = changeDateAction(uiState.queryDate.minus(1, DateTimeUnit.DAY)),
                    onReset = changeDateAction(DateTimeUtil.currentDate)
                )
            }
        ) {
            when {
                uiState.error -> ErrorView()
                !uiState.groupIsSelected -> {
                    SelectGroupView(
                        onClick = actionStartActivity(
                            Intent().apply {
                                component = ComponentName(context, MAIN_ACTIVITY_CLASS)
                            }
                        ),
                        modifier = GlanceModifier.fillMaxSize()
                    )
                }
                uiState.currentLessons.isNotEmpty() -> {
                    DaySchedule(
                        uiState = uiState,
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
                uiState.currentLessons.isEmpty() -> {
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
        onReset: Action,
    ) {
        val size = LocalSize.current

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(
                    horizontal = when {
                        SDK_INT < 31 -> 14.dp
                        size.width >= HORIZONTAL_RECTANGLE.width -> 12.dp
                        else -> 8.dp
                    },
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
                    .height(40.dp)
                    .defaultWeight(),
                onClick = onReset,
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
    private fun SelectGroupView(
        onClick: Action,
        modifier: GlanceModifier = GlanceModifier
    ) {
        val context = LocalContext.current

        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Button(
                text = context.getString(R.string.select_group),
                onClick = onClick,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorScheme.brand,
                    contentColor = colorScheme.onBrand,
                ),
            )
        }
    }

    @Composable
    private fun DaySchedule(
        uiState: ScheduleWidgetState,
        modifier: GlanceModifier = GlanceModifier,
    ) {
        val size = LocalSize.current
        val context = LocalContext.current
        val action = actionStartActivity(
            Intent.makeMainActivity(ComponentName(context, MAIN_ACTIVITY_CLASS)).apply {
                data = "$HOME_DEEPLINK/${uiState.queryDate}".toUri()
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxSize()
        ) {
            uiState.currentLessons.forEachIndexed { index, lesson ->
                item {
                    GlanceLesson(
                        lesson = lesson,
                        opacity = uiState.options.opacity,
                        background = context.pickSurfaceDrawableId(
                            themeMode = uiState.options.themeMode,
                            index = index,
                            size = uiState.currentLessons.size
                        ),
                        colorScheme = colorScheme,
                        modifier = GlanceModifier
                            .clickable(action)
                    )
                }

                item {
                    Spacer(GlanceModifier.height(4.dp))
                }
            }

            item {
                val height = if (size.width >= HORIZONTAL_RECTANGLE.width) 8.dp else 2.dp
                Spacer(GlanceModifier.height(height))
            }
        }
    }

    @Composable
    private fun ErrorView() {
        val context = LocalContext.current

        Column(
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically,
            modifier = GlanceModifier.fillMaxSize()
        ) {
            Image(
                provider = ImageProvider(R.drawable.ic_triangle_warning),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorScheme.brand),
                modifier = GlanceModifier.size(64.dp)
            )
            Spacer(GlanceModifier.height(8.dp))
            Text(
                text = LocalContext.current.getString(R.string.error_happend),
                style = TextStyle(
                    color = colorScheme.brand,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            )
            Spacer(GlanceModifier.height(8.dp))
            Button(
                text = context.getString(R.string.error_happend),
                onClick = changeDateAction(DateTimeUtil.currentDate),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorScheme.brand,
                    contentColor = colorScheme.onBrand,
                ),
            )
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        super.onCompositionError(context, glanceId, appWidgetId, throwable)
        runBlocking {
            context.updateScheduleWidgetState(
                glanceId = glanceId,
                updateState = {
                    WidgetStatePb.getDefaultInstance().copy {
                        error = true
                        isLoading = false
                    }
                }
            )
        }
    }

    private fun changeDateAction(date: LocalDate): Action {
        return actionRunCallback<ChangeDateCallback>(
            actionParametersOf(
                dateKey to date.toString()
            )
        )
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