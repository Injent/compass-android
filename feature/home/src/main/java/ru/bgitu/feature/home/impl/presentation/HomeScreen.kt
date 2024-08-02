package ru.bgitu.feature.home.impl.presentation

import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.plus
import kotlinx.datetime.toKotlinLocalDate
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.data.model.ScheduleLoadState
import ru.bgitu.core.designsystem.components.AppBottomBarTokens
import ru.bgitu.core.designsystem.components.AppIconButton
import ru.bgitu.core.designsystem.components.AppTab
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.theme.NoRippleTheme
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.Lesson
import ru.bgitu.core.model.Subject
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.push
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.core.ui.model.UiLesson
import ru.bgitu.core.ui.model.toUiModel
import ru.bgitu.core.ui.schedule.CalendarTheme
import ru.bgitu.core.ui.schedule.DayOfWeekSelector
import ru.bgitu.core.ui.schedule.DayOfWeekSelectorUiState
import ru.bgitu.core.ui.schedule.LessonItem
import ru.bgitu.core.ui.schedule.LoadingLessonItems
import ru.bgitu.feature.home.R
import ru.bgitu.feature.home.impl.presentation.components.NewFeaturesButton
import ru.bgitu.feature.home.impl.presentation.components.SelectGroupView
import kotlin.random.Random

@Composable
fun HomeScreen(
    initialScheduleDate: LocalDate? = null,
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val viewModel: HomeViewModel = koinViewModel(
        viewModelStoreOwner = context as ComponentActivity // should create only once per app launch
    ) {
        parametersOf(initialScheduleDate)
    }

    viewModel.events.listenEvents { event ->
        when (event) {
            HomeEvent.NavigateToGroupSettings -> navController.push(Screen.Groups)
            HomeEvent.NavigateToNewFeatures -> navController.push(Screen.About)
        }
    }

    val selectorUiState by viewModel.selectorUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scheduleState by viewModel.scheduleState.collectAsStateWithLifecycle()
    val classLabelMessage by viewModel.classLabelMessage.collectAsStateWithLifecycle()
    val hasNewFeatures by viewModel.hasNewFeatures.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        selectorUiState = selectorUiState,
        onIntent = viewModel::onIntent,
        scheduleState = scheduleState,
        classLabelMessage = classLabelMessage,
        hasNewFeatures = hasNewFeatures,
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    selectorUiState: DayOfWeekSelectorUiState,
    onIntent: (HomeIntent) -> Unit,
    scheduleState: ScheduleLoadState,
    classLabelMessage: TextResource?,
    hasNewFeatures: Boolean,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = selectorUiState.initialPage,
        pageCount = { selectorUiState.pageCount }
    )

    Scaffold(
        topBar = {
            HomeScreenTopBar(
                selectorState = selectorUiState,
                onDateSelect = { date ->
                    scope.launch {
                        val page = selectorUiState.getPageForDate(date)
                        pagerState.scrollToPage(page)
                    }
                }
            )
        },
        bottomBar = {
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                modifier = Modifier
                    .fillMaxWidth()
                    .thenIf(uiState is HomeUiState.Success || hasNewFeatures) {
                        padding(
                            start = AppTheme.spacing.m,
                            end = AppTheme.spacing.m,
                            bottom = AppTheme.spacing.m
                        )
                    }
            ) {
                if (hasNewFeatures) {
                    NewFeaturesButton(
                        onClick = { onIntent(HomeIntent.NavigateToNewFeatures) },
                        modifier = Modifier
                            .align(Alignment.End)
                    )
                }

                when (uiState) {
                    is HomeUiState.Success -> {
                        if (uiState.savedGroups.size > 1 && uiState.showGroups
                            && uiState.selectedGroup != null) {
                            GroupTabs(
                                groups = uiState.savedGroups,
                                selectedGroup = uiState.selectedGroup,
                                onGroupSelected = { group -> onIntent(HomeIntent.ChangeGroupView(group)) },
                                onGroupSettingsClick = { onIntent(HomeIntent.NavigateToGroupSettings) },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                    else -> Unit
                }
            }
        },
        modifier = Modifier
            .systemBarsPadding()
            .padding(bottom = AppBottomBarTokens.Height)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            var ignorePageChangeOnInit by rememberSaveable { mutableStateOf(true) }

            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.conflate().collect {
                    if (!ignorePageChangeOnInit) {
                        val date = selectorUiState.getDateForPage(it)
                        onIntent(HomeIntent.ChangeDate(date))
                    } else {
                        ignorePageChangeOnInit = false
                    }
                }
            }

            DayOfWeekSelector(
                state = selectorUiState,
                onDateSelected = { date ->
                    scope.launch {
                        val page = selectorUiState.getPageForDate(date)
                        pagerState.animateScrollToPage(page)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.l)
            )
            Spacer(Modifier.height(AppTheme.spacing.l))

            when (uiState) {
                HomeUiState.GroupNotSelected -> {
                    SelectGroupView(
                        onClick = { onIntent(HomeIntent.NavigateToGroupSettings) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(AppTheme.spacing.xxxl)
                    )
                }
                HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    HorizontalPager(
                        state = pagerState,
                        pageSpacing = AppTheme.spacing.l,
                        contentPadding = PaddingValues(horizontal = AppTheme.spacing.l)
                    ) { page ->
                        val scrollState = rememberScrollState()

                        Column(
                            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            val maxWidthModifier = Modifier.fillMaxWidth()

                            when (scheduleState) {
                                is ScheduleLoadState.Error -> {
                                    AlertSchedule(
                                        modifier = maxWidthModifier,
                                        illustation = AppIllustrations.Warning,
                                        headline = stringResource(R.string.error_failed_to_load_data),
                                    )
                                }
                                ScheduleLoadState.Loading -> LoadingLessonItems(modifier = maxWidthModifier)
                                is ScheduleLoadState.Success -> {
                                    val mapedLessons = remember(scheduleState, selectorUiState) {
                                        val dateForPage = selectorUiState.getDateForPage(page)
                                        scheduleState[dateForPage].let { lessons ->
                                            lessons.map {
                                                it.toUiModel(
                                                    isFirst = lessons.first().startAt == it.startAt,
                                                    isLast = lessons.last().startAt == it.startAt
                                                )
                                            }
                                        }
                                    }
                                    if (mapedLessons.isEmpty()) {
                                        AlertSchedule(
                                            modifier = maxWidthModifier,
                                            illustation = AppIllustrations.Calendar,
                                            headline = stringResource(R.string.weekend),
                                        )
                                    } else {
                                        DaySchedule(
                                            lessons = mapedLessons,
                                            classLabelMessage = classLabelMessage.takeIf {
                                                selectorUiState.currentDateTime.date == mapedLessons.first().date
                                            },
                                            currentDateTime = selectorUiState.currentDateTime
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AlertSchedule(
    @DrawableRes illustation: Int,
    headline: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(AppTheme.spacing.xl),
    ) {
        Image(
            painter = painterResource(illustation),
            contentDescription = null
        )
        Text(
            text = headline,
            style = AppTheme.typography.title3,
            color = AppTheme.colorScheme.foreground1
        )
    }
}

@Composable
private fun ColumnScope.DaySchedule(
    lessons: List<UiLesson>,
    classLabelMessage: TextResource?,
    currentDateTime: LocalDateTime
) {
    AnimatedVisibility(
        visible = classLabelMessage != null,
        enter = expandVertically(tween(500, easing = LinearEasing)) + fadeIn(),
        exit = ExitTransition.None
    ) {
        classLabelMessage?.let {
            Surface(
                color = AppTheme.colorScheme.foreground,
                shape = AppTheme.shapes.default,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.spacing.s)
            ) {
                Box(
                    Modifier.padding(
                        horizontal = AppTheme.spacing.s,
                        vertical = AppTheme.spacing.xs
                    )
                ) {
                    Text(
                        text = it.asString(),
                        style = AppTheme.typography.callout,
                        color = AppTheme.colorScheme.foregroundOnBrand,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }
        }
    }

    lessons.forEach {
        var expanded by remember { mutableStateOf(false) }
        LessonItem(
            lesson = it,
            onClick = { expanded = !expanded },
            expanded = expanded,
            now = currentDateTime,
            modifier = Modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenTopBar(
    selectorState: DayOfWeekSelectorUiState,
    onDateSelect: (LocalDate) -> Unit
) {
    val calendarState = rememberUseCaseState(visible = false)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = AppTheme.spacing.l)
    ) {
        val context = LocalContext.current
        val actualScheduleDate = remember(selectorState) {
            selectorState.currentDateTime.date.let {
                if (it.dayOfWeek == DayOfWeek.SUNDAY)
                    it.plus(1, DateTimeUnit.DAY)
                else it
            }
        }
        val showButton = remember(selectorState) {
            selectorState.selectedDate != actualScheduleDate
        }
        val buttonText = remember(selectorState) {
            if (selectorState.currentDateTime.dayOfWeek == DayOfWeek.SUNDAY) {
                context.getString(R.string.tomorrow)
            } else {
                context.getString(R.string.today)
            }
        }
        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200)),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            AppTextButton(
                text = buttonText,
                onClick = {
                    onDateSelect(actualScheduleDate)
                }
            )
        }

        var isNextMonth by remember { mutableStateOf(false) }
        var date by remember { mutableStateOf(selectorState.selectedDate) }
        val month = remember(date) {
            DateTimeUtil.formatMonth(date)
        }

        LaunchedEffect(selectorState) {
            if (date != selectorState.selectedDate) {
                isNextMonth = selectorState.selectedDate > date
                date = selectorState.selectedDate
            }
        }

        AnimatedContent(
            targetState = month,
            label = "",
            transitionSpec = {
                val tweenSpec = tween<Float>(durationMillis = 100, easing = FastOutSlowInEasing)
                slideInVertically {
                    if (isNextMonth) it
                    else -it
                } + fadeIn(tweenSpec) togetherWith slideOutVertically {
                    if (isNextMonth) -it
                    else it
                } + fadeOut(tweenSpec)
            },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = it,
                    style = AppTheme.typography.title3,
                    color = AppTheme.colorScheme.foreground1,
                    softWrap = false,
                    maxLines = 1
                )
                val weekNumber = remember(selectorState) {
                    "(" + DateTimeUtil.getFormatedStudyWeekNumber(selectorState.selectedDate)
                        .asString(context) + ")"
                }
                Text(
                    text = weekNumber,
                    style = AppTheme.typography.footstrike,
                    color = AppTheme.colorScheme.foreground2,
                    modifier = Modifier.offset(y = -(2.dp))
                )
            }
        }
        AppIconButton(
            onClick = calendarState::show,
            icon = AppIcons.Calendar2,
            tint = AppTheme.colorScheme.foreground,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }

    CalendarTheme {
        CalendarDialog(
            state = calendarState,
            selection = CalendarSelection.Date { date ->
                onDateSelect(
                    date.toKotlinLocalDate().let {
                        if (it.dayOfWeek == DayOfWeek.SUNDAY)
                            it.plus(1, DateTimeUnit.DAY)
                        else it
                    }
                )
            },
            config = CalendarConfig(
                yearSelection = false,
                boundary = selectorState.dateBoundary,
                style = CalendarStyle.MONTH
            )
        )
    }
}

@Composable
private fun GroupTabs(
    groups: List<Group>,
    selectedGroup: Group,
    onGroupSelected: (Group) -> Unit,
    onGroupSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = AppTheme.colorScheme.background4,
                shape = AppTheme.shapes.default
            )
    ) {
        AppIconButton(
            onClick = onGroupSettingsClick,
            icon = AppIcons.Swap,
            tint = AppTheme.colorScheme.foreground3
        )
        ScrollableTabRow(
            containerColor = Color.Transparent,
            selectedTabIndex = groups.indexOf(selectedGroup),
            edgePadding = 0.dp,
            modifier = modifier,
            indicator = { tabPositions ->
                val tabIndex = remember(groups, selectedGroup, tabPositions) {
                    tabPositions[groups.indexOf(selectedGroup).coerceAtLeast(0)]
                }
                Spacer(
                    modifier = Modifier
                        .tabIndicatorOffset(tabIndex)
                        .zIndex(-1f)
                        .fillMaxHeight()
                        .background(
                            color = AppTheme.colorScheme.background1,
                            shape = AppTheme.shapes.default
                        )
                )
            },
            divider = {}
        ) {
            AppRippleTheme(NoRippleTheme) {
                groups.forEachIndexed { index, group ->
                    val selected = selectedGroup == group

                    AppTab(
                        text = group.name,
                        selected = selected,
                        onClick = { onGroupSelected(group) }
                    )
                }
            }
        }
    }

}

class ScheduleStateParameterProvider : PreviewParameterProvider<ScheduleLoadState> {
    override val values: Sequence<ScheduleLoadState>
        get() {
            val now = DateTimeUtil.currentDate
            return sequenceOf(
                ScheduleLoadState.Success(
                    data = mapOf(now to createRandomLessons(now))
                )
            )
        }

    private fun createRandomLessons(date: LocalDate): List<Lesson> {
        return (0..3).map { lessonId ->
            val random = Random(lessonId)
            val startTime = LocalTime(lessonId + 8, 30)
            Lesson(
                lessonId = lessonId,
                building = random.nextInt(1, 2).toString(),
                date = date,
                subject = Subject(lessonId, "Sample Subject"),
                startAt = startTime,
                endAt = LocalTime(startTime.hour + 1, 0),
                classroom = random.nextInt(100, 430).toString(),
                teacher = "Teacher. S.A",
                isLecture = random.nextBoolean()
            )
        }
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@PreviewFontScale
@Composable
fun HomePreview(
    @PreviewParameter(ScheduleStateParameterProvider::class) scheduleState: ScheduleLoadState
) {
    CompassTheme {
        HomeScreenContent(
            selectorUiState = DayOfWeekSelectorUiState(DateTimeUtil.currentDateTime, DateTimeUtil.currentDate),
            onIntent = {},
            scheduleState = scheduleState,
            classLabelMessage = TextResource.Plain("Class text"),
            hasNewFeatures = true,
            uiState = HomeUiState.Loading,
        )
    }
}