package ru.bgitu.feature.home.impl.presentation

import android.view.SoundEffectConstants
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.EaseInOutExpo
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.ScreenRotation
import ru.bgitu.core.common.eventbus.EventBus
import ru.bgitu.core.common.screenRotation
import ru.bgitu.core.data.model.ScheduleState
import ru.bgitu.core.data.model.getLessons
import ru.bgitu.core.datastore.model.StoredLesson
import ru.bgitu.core.designsystem.components.AppConfirmButton
import ru.bgitu.core.designsystem.components.AppDialog
import ru.bgitu.core.designsystem.components.AppIconButton
import ru.bgitu.core.designsystem.components.AppSnackbarHost
import ru.bgitu.core.designsystem.components.AppTab
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.components.CompassLoading
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.CalendarOutlined
import ru.bgitu.core.designsystem.icon.Swap
import ru.bgitu.core.designsystem.illustration.AppIllustrations
import ru.bgitu.core.designsystem.illustration.BrokenBulb
import ru.bgitu.core.designsystem.illustration.Calendar
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.designsystem.theme.bottom
import ru.bgitu.core.designsystem.theme.end
import ru.bgitu.core.designsystem.util.MeasureComposable
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.model.Group
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.push
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.core.ui.schedule.CalendarTheme
import ru.bgitu.feature.home.R
import ru.bgitu.feature.home.impl.model.DayOfWeekSelectorUiState
import ru.bgitu.feature.home.impl.presentation.component.DateSelectorPager
import ru.bgitu.feature.home.impl.presentation.component.LessonItem
import ru.bgitu.feature.home.impl.presentation.component.LessonVisualData
import ru.bgitu.feature.home.impl.presentation.component.SelectGroupView

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
            is HomeEvent.NavigateToCreateNote -> navController.push(
                Screen.NoteDetails(subjectName = event.subjectName)
            )
        }
    }

    val selectorUiState by viewModel.selectorUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scheduleUiState by viewModel.scheduleUiState.collectAsStateWithLifecycle()
    val classLabelMessage by viewModel.classLabelMessage.collectAsStateWithLifecycle()
    val shouldShowDataResetAlert = remember(uiState) {
        (uiState as? HomeUiState.GroupNotSelected)?.shouldShowDataResetAlert ?: false
    }

    HomeScreenContent(
        uiState = uiState,
        selectorUiState = selectorUiState,
        onIntent = viewModel::onIntent,
        classLabelMessage = classLabelMessage?.asString(),
        shouldShowDataResetAlert = shouldShowDataResetAlert,
        scheduleUiState = scheduleUiState
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    selectorUiState: DayOfWeekSelectorUiState,
    onIntent: (HomeIntent) -> Unit,
    classLabelMessage: String?,
    shouldShowDataResetAlert: Boolean,
    scheduleUiState: ScheduleState
) {
    val context = LocalContext.current
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val scope = rememberCoroutineScope()
    val datePagerState = rememberPagerState(
        initialPage = selectorUiState.dateInitialPage,
        pageCount = { selectorUiState.datePageCount }
    )
    val pagerState = rememberPagerState(
        initialPage = remember { selectorUiState.initialPage },
        pageCount = { selectorUiState.pageCount }
    )
    var playingTransitionAnimation by remember { mutableStateOf(false) }
    val animatedScaleOfScheduleTransition = remember {
        Animatable(1f)
    }

    if (shouldShowDataResetAlert) {
        DataResetAlertDialog(
            onDismiss = {
                onIntent(HomeIntent.DismissPickGroupQuery)
            },
            onConfirm = {
                onIntent(HomeIntent.PickGroup)
            }
        )
    }
    LaunchedEffect(Unit) {
        EventBus.subscribe<ChangeGroupView> { event ->
            playingTransitionAnimation = true
            animatedScaleOfScheduleTransition.animateTo(
                targetValue = 0.95f,
                animationSpec = tween()
            )
            onIntent(HomeIntent.ChangeGroupView(event.group))
            playingTransitionAnimation = false
            animatedScaleOfScheduleTransition.animateTo(
                targetValue = 1f,
                animationSpec = tween()
            )
        }
    }

    Scaffold(
        topBar = {
            HomeScreenTopBar(
                selectorState = selectorUiState,
                onDateSelect = { date ->
                    scope.launch {
                        pagerState.animateScaleAndScroll(
                            animatedScaleOfScheduleTransition,
                            selectorUiState.getPage(date)
                        )
                    }
                    scope.launch {
                        datePagerState.animateScrollToPage(
                            selectorUiState.getPage(date) / 6)
                    }
                }
            )
        },
        snackbarHost = {
            AppSnackbarHost(Modifier.padding(bottom = AppTheme.spacing.l))
        },
        bottomBar = {
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                modifier = Modifier
                    .thenIf(uiState is HomeUiState.Success) {
                        padding(
                            start = AppTheme.spacing.m,
                            end = AppTheme.spacing.m,
                            bottom = AppTheme.spacing.m
                        )
                    }
            ) {
                when (uiState) {
                    is HomeUiState.Success -> {
                        if (uiState.showGroups && uiState.selectedGroup != null &&
                            windowAdaptiveInfo.windowSizeClass
                                .widthSizeClass <= WindowWidthSizeClass.Compact) {
                            GroupTabs(
                                groups = uiState.savedGroups,
                                selectedGroup = uiState.selectedGroup,
                                onGroupSelected = { group ->
                                    scope.launch {
                                        EventBus.post(ChangeGroupView(group))
                                    }
                                },
                                onGroupSettingsClick = { onIntent(HomeIntent.NavigateToGroupSettings) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    is HomeUiState.Loading -> {
                        if (!playingTransitionAnimation) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CompassLoading(loadingSize = 32.dp)
                            }
                        }
                    }
                    else -> Unit
                }
            }
        },
        modifier = Modifier
            .systemBarsPadding()
            .padding(LocalExternalPadding.current)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(innerPadding.calculateTopPadding()))
            var ignorePageChangeOnInit by rememberSaveable { mutableStateOf(true) }

            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.conflate().collect {
                    if (!ignorePageChangeOnInit) {
                        val date = selectorUiState.getDate(it)
                        onIntent(HomeIntent.ChangeDate(date))

                        datePagerState.animateScrollToPage(
                            page = selectorUiState.getPageFromDatePager(date, it),
                            animationSpec = tween(durationMillis = 600, easing = EaseInOutExpo)
                        )
                    } else {
                        ignorePageChangeOnInit = false
                    }
                }
            }

            DateSelectorPager(
                state = selectorUiState,
                pagerState = datePagerState,
                onDateSelect = { date ->
                    scope.launch {
                        pagerState.animateScaleAndScroll(
                            animatedScaleOfScheduleTransition,
                            selectorUiState.getPage(date)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .thenIf(context.screenRotation == ScreenRotation.RIGHT) {
                        displayCutoutPadding()
                    }
            )

            Spacer(Modifier.height(AppTheme.spacing.l))

            when (uiState) {
                is HomeUiState.GroupNotSelected -> {
                    SelectGroupView(
                        onClick = { onIntent(HomeIntent.NavigateToGroupSettings) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(AppTheme.spacing.xxxl)
                    )
                }
                HomeUiState.Loading -> Unit
                is HomeUiState.Success -> {
                    val endPadding = if (context.screenRotation == ScreenRotation.RIGHT) {
                        WindowInsets.displayCutout.asPaddingValues().end + AppTheme.spacing.l
                    } else AppTheme.spacing.l
                    HorizontalPager(
                        state = pagerState,
                        pageSpacing = endPadding,
                        beyondViewportPageCount = 0,
                        contentPadding = PaddingValues(
                            start = AppTheme.spacing.l,
                            end = endPadding
                        ),
                    ) { page ->
                        val generalModifier = Modifier
                            .fillMaxSize()
                            .scale(animatedScaleOfScheduleTransition.value)

                        val alertScheduleModifier = Modifier
                            .padding(bottom = innerPadding.bottom)

                        when (scheduleUiState) {
                            is ScheduleState.Error -> {
                                AlertSchedule(
                                    modifier = generalModifier.then(alertScheduleModifier),
                                    illustration = AppIllustrations.BrokenBulb,
                                    headline = scheduleUiState.details.asString(),
                                    description = ""
                                )
                            }
                            is ScheduleState.Loaded -> {
                                val pageDate = remember(page) {
                                    selectorUiState.getDate(page)
                                }
                                val lessons = scheduleUiState.getLessons(pageDate)

                                if (lessons.isEmpty()) {
                                    AlertSchedule(
                                        modifier = generalModifier.then(alertScheduleModifier),
                                        illustration = AppIllustrations.Calendar,
                                        headline = stringResource(R.string.weekend),
                                        description = stringResource(R.string.no_classes_today)
                                    )
                                } else {
                                    Column(
                                        modifier = generalModifier.verticalScroll(rememberScrollState())
                                    ) {
                                        DaySchedule(
                                            lessons = lessons,
                                            classLabelMessage = remember(classLabelMessage) {
                                                classLabelMessage.takeIf {
                                                    selectorUiState.now.date == pageDate
                                                }
                                            },
                                            onAddNote = {

                                            },
                                            now = selectorUiState.now,
                                            lessonsDate = pageDate
                                        )
                                        Spacer(Modifier.height(innerPadding.bottom + AppTheme.spacing.l))
                                    }
                                }
                            }
                            ScheduleState.Loading -> {
                                Box(
                                    modifier = generalModifier
                                        .then(alertScheduleModifier)
                                        .padding(top = AppTheme.spacing.xxxl)
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CompassLoading(loadingSize = 32.dp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun AlertSchedule(
    illustration: ImageVector,
    headline: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    if (windowAdaptiveInfo.windowSizeClass.widthSizeClass <= WindowWidthSizeClass.Compact) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.spacing.xl),
        ) {
            Image(
                imageVector = illustration,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth(),
            )
            Spacer(Modifier.height(AppTheme.spacing.l))
            Text(
                text = headline,
                style = AppTheme.typography.title3,
                textAlign = TextAlign.Center,
                color = AppTheme.colorScheme.foreground1
            )
            Text(
                text = description,
                style = AppTheme.typography.callout,
                color = AppTheme.colorScheme.foreground2,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(AppTheme.spacing.xl)
        ) {
            Image(
                imageVector = illustration,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(max = 250.dp),
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = headline,
                    style = AppTheme.typography.title3,
                    textAlign = TextAlign.Center,
                    color = AppTheme.colorScheme.foreground1
                )
                Spacer(Modifier.height(AppTheme.spacing.s))
                Text(
                    text = description,
                    style = AppTheme.typography.callout,
                    color = AppTheme.colorScheme.foreground2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.DaySchedule(
    lessons: List<StoredLesson>,
    onAddNote: (StoredLesson) -> Unit,
    classLabelMessage: String?,
    now: LocalDateTime,
    lessonsDate: LocalDate
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
                    .padding(bottom = AppTheme.spacing.l)
            ) {
                Box(
                    Modifier.padding(
                        horizontal = AppTheme.spacing.s,
                        vertical = AppTheme.spacing.xs
                    )
                ) {
                    Text(
                        text = it,
                        style = AppTheme.typography.callout,
                        color = AppTheme.colorScheme.foregroundOnBrand,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }
        }
    }

    MeasureComposable(
        composable = {
            Text(text = "0".repeat(5), style = AppTheme.typography.headline2)
        }
    ) { minTimeSize ->
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
        ) {
            var expandedLesson: StoredLesson? by remember { mutableStateOf(null) }

            lessons.forEachIndexed { index, lesson ->
                val data = remember(lesson) {
                    LessonVisualData(
                        isFirst = index == 0,
                        isLast = index == lessons.lastIndex,
                        isPassed = lessonsDate.atTime(lesson.endAt) <= now,
                        isHighlighted = lesson.isHighlighted(now, lessonsDate),
                        minTimeWidth = minTimeSize.width
                    )
                }

                LessonItem(
                    lesson = lesson,
                    lessonDate = lessonsDate,
                    data = data,
                    onClick = {
                        if (expandedLesson == lesson) {
                            expandedLesson = null
                            return@LessonItem
                        }
                        expandedLesson = lesson
                    },
                    onAddNote = { onAddNote(lesson) },
                    expanded = lesson == expandedLesson,
                    now = now,
                    modifier = Modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenTopBar(
    selectorState: DayOfWeekSelectorUiState,
    onDateSelect: (LocalDate) -> Unit
) {
    val calendarState = rememberUseCaseState(visible = false)
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = AppTheme.spacing.l)
            .thenIf(context.screenRotation == ScreenRotation.RIGHT) {
                displayCutoutPadding()
            }
    ) {
        val actualScheduleDate = remember(selectorState) {
            selectorState.now.date.let {
                if (it.dayOfWeek == DayOfWeek.SUNDAY)
                    it.plus(1, DateTimeUnit.DAY)
                else it
            }
        }
        val showButton = remember(selectorState) {
            selectorState.selectedDate != actualScheduleDate
        }
        val buttonText = remember(selectorState) {
            if (selectorState.now.dayOfWeek == DayOfWeek.SUNDAY) {
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
                    "(" + DateTimeUtil.getFormattedStudyWeekNumber(selectorState.selectedDate)
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
            icon = AppIcons.CalendarOutlined,
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
                style = CalendarStyle.MONTH,
                boundary = java.time.LocalDate.now().let {
                    it.minusYears(1)..it.plusYears(1)
                },
                disabledDates = remember {
                    val sundays = mutableListOf<java.time.LocalDate>()

                    val now = selectorState.now.toJavaLocalDateTime()
                    var firstSunday = java.time.LocalDate.of(now.year - 1, 1, 1)
                        .with(DayOfWeek.SUNDAY)

                    while (firstSunday.year in (now.year - 1)..now.year + 1) {
                        sundays.add(firstSunday)
                        firstSunday = firstSunday.plusWeeks(1)
                    }

                    sundays
                }
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
    val view = LocalView.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
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

        val tabs = @Composable {
            AppRippleTheme(null) {
                groups.forEach { group ->
                    AppTab(
                        text = group.name,
                        selected = selectedGroup == group,
                        onClick = {
                            onGroupSelected(group)
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                        }
                    )
                }
            }
        }
        ScrollableTabRow(
            containerColor = Color.Transparent,
            selectedTabIndex = remember(groups, selectedGroup) { groups.indexOf(selectedGroup) },
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                val tabIndex = remember(groups, selectedGroup, tabPositions) {
                    runCatching {
                        tabPositions[groups.indexOf(selectedGroup).coerceIn(0, groups.size - 1)]
                    }.getOrDefault(tabPositions.first())
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
            divider = {},
            tabs = tabs
        )
    }
}

@Composable
private fun DataResetAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppDialog(
        modifier = modifier,
        title = stringResource(R.string.title_reset_group),
        onDismissRequest = onDismiss,
        buttons = {
            AppConfirmButton(
                text = stringResource(R.string.select),
                onClick = onConfirm,
                modifier = Modifier.weight(1f)
            )
        }
    ) {
        Text(
            text = stringResource(R.string.dialog_description_reset_group),
            style = AppTheme.typography.body,
            color = AppTheme.colorScheme.foreground1
        )
    }
}

private suspend fun PagerState.animateScaleAndScroll(
    animatable: Animatable<Float, AnimationVector1D>,
    page: Int,
) {
    if (page == this.currentPage) return
    animatable.animateTo(
        targetValue = 0.95f,
        animationSpec = tween()
    )
    this.scrollToPage(page)
    animatable.animateTo(
        targetValue = 1f,
        animationSpec = tween()
    )
}

private fun StoredLesson.isHighlighted(now: LocalDateTime, lessonDate: LocalDate): Boolean {
    val start = startAt.atDate(lessonDate)
    val end = endAt.atDate(lessonDate)
    return now in start..end
}