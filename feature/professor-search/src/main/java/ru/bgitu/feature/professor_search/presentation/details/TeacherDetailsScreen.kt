package ru.bgitu.feature.professor_search.presentation.details

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.core.common.CommonStrings
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.LessonDataUtils
import ru.bgitu.core.common.iterator
import ru.bgitu.core.designsystem.components.AppChip
import ru.bgitu.core.designsystem.components.AppIconButton
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.components.CompassLoading
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.model.ProfessorClass
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.professor_search.R
import ru.bgitu.feature.professor_search.presentation.KEY_TEACHER_VIEWMODEL
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

@Composable
fun TeacherDetailsScreen(
    teacherName: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: TeacherDetailsViewModel = koinViewModel(
        key = KEY_TEACHER_VIEWMODEL,
        viewModelStoreOwner = context as ComponentActivity,
        parameters = { parametersOf(teacherName) }
    )
    val snackbarController = LocalSnackbarController.current

    viewModel.events.listenEvents { event ->
        when (event) {
            is ProfessorDetailsEvent.ShowError -> {
                snackbarController.show(
                    duration = 3.seconds,
                    message = event.errorDetails.asString(context),
                    withDismissAction = false,
                    icon = AppIcons.WarningRed,
                )
            }
            ProfessorDetailsEvent.Back -> {
                onBack()
            }
        }
    }

    BackHandler {
        onBack()
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfessorDetailsScreenContent(
        uiState = uiState,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun ProfessorDetailsScreenContent(
    uiState: ProfessorDetailsUiState,
    onIntent: (ProfessorDetailsIntent) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ProfessorDetailsTopBar(
                uiState = uiState,
                onIntent = onIntent
            )
        },
        modifier = Modifier
            .statusBarsPadding()
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = if (uiState.isLoading) {
                Arrangement.Center
            } else Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                start = AppTheme.spacing.s,
                end = AppTheme.spacing.s,
                bottom = AppTheme.spacing.s +
                        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val schedule = uiState.filteredSchedule) {
                is FilteredSchedule.ByDays -> {
                    if (schedule.selectedDate != null) {
                        daySchedule(
                            classes = schedule.classes,
                            date = schedule.selectedDate
                        )
                    } else {
                        unfilteredClassesList(
                            dateBoundary = uiState.dateBoundary,
                            classes = schedule.classes
                        )
                    }
                }
                is FilteredSchedule.ByWeek -> {
                    for ((date, lessons) in schedule.schedules) {
                        daySchedule(lessons, date)
                    }
                    if (schedule.schedules.isEmpty()) {
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    text = remember {
                                        context.getString(
                                            R.string.from_date_to_date,
                                            DateTimeUtil.formatDate(uiState.fromDate),
                                            DateTimeUtil.formatDate(uiState.toDate)
                                        )
                                    },
                                    style = AppTheme.typography.callout,
                                    color = AppTheme.colorScheme.foreground2,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(vertical = 12.dp)
                                )
                                Spacer(Modifier.height(AppTheme.spacing.xl))
                                Icon(
                                    painter = painterResource(AppIllustrations.CalendarClock),
                                    contentDescription = null,
                                    tint = AppTheme.colorScheme.foreground3,
                                    modifier = Modifier
                                        .size(64.dp)
                                )
                                Text(
                                    text = stringResource(R.string.not_holding_classes),
                                    style = AppTheme.typography.subheadline,
                                    fontWeight = FontWeight.Normal,
                                    color = AppTheme.colorScheme.foreground3
                                )
                            }
                        }
                    }
                }
                else -> loading()
            }
        }
    }
}

private fun LazyListScope.loading() {
    item {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CompassLoading()
        }
    }
}

private fun LazyListScope.unfilteredClassesList(
    dateBoundary: ClosedRange<LocalDate>,
    classes: List<ProfessorClass>
) {
    dateBoundary.iterator(unit = DateTimeUnit.DAY).forEach { date ->
        daySchedule(
            classes = classes.filter { it.date == date },
            date = date
        )
    }
}

private fun LazyListScope.daySchedule(
    classes: List<ProfessorClass>,
    date: LocalDate
) {
    item {
        Text(
            text = remember(date) { DateTimeUtil.formatDate(date) },
            style = AppTheme.typography.callout,
            color = AppTheme.colorScheme.foreground2,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
    if (classes.isEmpty()) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.spacing.xl),
            ) {
                Icon(
                    painter = painterResource(AppIllustrations.CalendarClock),
                    contentDescription = null,
                    tint = AppTheme.colorScheme.foreground3,
                    modifier = Modifier
                        .size(64.dp)
                )
                Text(
                    text = stringResource(R.string.not_holding_classes),
                    style = AppTheme.typography.subheadline,
                    fontWeight = FontWeight.Normal,
                    color = AppTheme.colorScheme.foreground3
                )
            }
        }
    } else {
        items(
            items = classes,
            key = { item -> item.id }
        ) { lesson ->
            ProfessorClassItem(
                lesson = lesson,
                isFirst = classes.first() == lesson,
                isLast = classes.last() == lesson,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProfessorClassItem(
    lesson: ProfessorClass,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val timeInterval = remember(lesson) {
        DateTimeUtil.formatTime(lesson.startAt) + "-" + DateTimeUtil.formatTime(lesson.endAt)
    }
    val classLocation = remember(lesson) {
        LessonDataUtils.provideLocation(
            context = context,
            building = lesson.building,
            classroom = lesson.classroom,
            style = LessonDataUtils.DisplayStyle.DEFAULT
        )
    }

    Surface(
        shape = when {
            isFirst && isLast -> AppTheme.shapes.default
            isFirst -> AppTheme.shapes.defaultTopCarved
            isLast -> AppTheme.shapes.defaultBottomCarved
            else -> RectangleShape
        },
        color = AppTheme.colorScheme.backgroundTouchable,
        modifier = modifier,
    ) {
        Column(Modifier.padding(AppTheme.spacing.l)) {
            Text(
                text = timeInterval,
                style = AppTheme.typography.callout,
                color = AppTheme.colorScheme.foreground2,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = classLocation,
                style = AppTheme.typography.headline2,
                color = AppTheme.colorScheme.foreground1
            )
            Text(
                text = stringResource(
                    if (lesson.isLecture) {
                        CommonStrings.lecture
                    } else CommonStrings.practice
                ),
                style = AppTheme.typography.footnote,
                color = AppTheme.colorScheme.foreground2.copy(alpha = .85f)
            )
        }
    }
    if (!isLast)
        Spacer(Modifier.height(1.5.dp))
}

@Composable
private fun ProfessorDetailsTopBar(
    uiState: ProfessorDetailsUiState,
    onIntent: (ProfessorDetailsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier.padding(bottom = AppTheme.spacing.s)) {
        TopAppBar(
            title = {
                Text(
                    text = uiState.professorName,
                    style = AppTheme.typography.title3,
                    color = AppTheme.colorScheme.foreground1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            navigationIcon = {
                AppIconButton(
                    onClick = { onIntent(ProfessorDetailsIntent.Back) },
                    icon = AppIcons.Back,
                    tint = AppTheme.colorScheme.foreground3
                )
            },
            actions = {
                AppTextButton(
                    text = stringResource(
                        if (uiState.filteredByWeek) {
                            R.string.filter_by_days
                        } else R.string.filter_by_weeks
                    ),
                    onClick = {
                        onIntent(ProfessorDetailsIntent.ChangeFilter(!uiState.filteredByWeek))
                    }
                )
            }
        )

        when (uiState.filteredSchedule) {
            is FilteredSchedule.ByDays -> {
                val scrollState = rememberLazyListState()
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                    contentPadding = PaddingValues(horizontal = AppTheme.spacing.l),
                    state = scrollState
                ) {
                    val dates = uiState.dateBoundary.iterator(unit = DateTimeUnit.DAY).asSequence().toList()
                    itemsIndexed(
                        items = dates,
                        key = { index, _ -> index }
                    ) { index, date ->
                        AppChip(
                            selected = date == uiState.filteredSchedule.selectedDate,
                            onClick = {
                                onIntent(ProfessorDetailsIntent.ChangeDate(date))
                                coroutineScope.launch {
                                    scrollState.animateScrollToItem((index - 1).coerceAtLeast(0))
                                }
                            },
                            label = remember(date) {
                                DateTimeUtil.formatDay(date)
                            }
                        )
                    }
                }
            }
            is FilteredSchedule.ByWeek -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.width(AppTheme.spacing.s))
                    DayOfWeek.entries.filterNot { it == DayOfWeek.SUNDAY }.map { dayOfWeek ->
                        AppChip(
                            label = dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault()),
                            onClick = {
                                onIntent(ProfessorDetailsIntent.ChangeWeek(dayOfWeek))
                            },
                            shape = AppTheme.shapes.default,
                            selected = dayOfWeek == uiState.filteredSchedule.selectedWeek,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.width(AppTheme.spacing.s))
                }
            }
            else -> Unit
        }
    }
}