package ru.bgitu.feature.professor_search.presentation.details

import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.ScreenRotation
import ru.bgitu.core.common.screenRotation
import ru.bgitu.core.designsystem.components.AppSnackbarHost
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.components.CompassLoading
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.illustration.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.designsystem.theme.SpotCard
import ru.bgitu.core.designsystem.theme.end
import ru.bgitu.core.designsystem.util.MeasureComposable
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.boxShadow
import ru.bgitu.core.designsystem.util.shadow.roundRectShadow
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.model.ProfessorClass
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.Headline
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.professor_search.R
import ru.bgitu.feature.professor_search.presentation.KEY_TEACHER_VIEWMODEL
import ru.bgitu.feature.professor_search.presentation.components.maxWidthOfDayAndWeek
import ru.bgitu.feature.professor_search.presentation.components.teacherClassesGroup
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Duration.Companion.INFINITE

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
                    duration = INFINITE,
                    message = event.errorDetails.asString(context),
                    withDismissAction = false,
                    icon = AppIcons.WarningRed,
                )
            }
            ProfessorDetailsEvent.Back -> {
                snackbarController.dismiss()
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
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = uiState.selectedPage,
        pageCount = { 6 }
    )

    var pagerInited by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!pagerInited) {
            pagerInited = true
            return@LaunchedEffect
        }

        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect {
            onIntent(ProfessorDetailsIntent.ChangePage(pagerState.currentPage))
        }
    }


    val isLandScape = LocalConfiguration.current
        .orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ProfessorDetailsTopBar(
                uiState = uiState,
                onPageSelect = { page ->
                    coroutineScope.launch {
                        withContext(NonCancellable) {
                            pagerState.animateScrollToPage(page)
                        }
                    }
                },
                onIntent = onIntent
            )
        },
        snackbarHost = {
            AppSnackbarHost(
                modifier = Modifier.offset(y = -AppTheme.spacing.l)
            )
        },
        modifier = Modifier
            .thenIf(isLandScape) {
                padding(LocalExternalPadding.current)
            }
    ) { paddingValues ->
        val maxWidthOfDayAndWeek by maxWidthOfDayAndWeek()

        var timeColumnWidth by remember { mutableStateOf(0.dp) }

        MeasureComposable(
            composable = {
                Text(
                    text = "00:00",
                    style = AppTheme.typography.callout
                )
            }
        ) { size ->
            timeColumnWidth = size.width
        }

        val classesListContent = @Composable { modifier: Modifier, page: Int? ->
            LazyColumn(
                verticalArrangement = if (uiState.loading) {
                    Arrangement.Center
                } else Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(
                    start = AppTheme.spacing.l,
                    end = AppTheme.spacing.l,
                    bottom = AppTheme.spacing.l
                ),
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (uiState.loading) {
                    loading()
                } else {
                    if (page == null) {
                        unfilteredClassesList(
                            classes = uiState.schedules,
                            maxWidthOfDayAndWeek = maxWidthOfDayAndWeek,
                            timeColumnWidth = timeColumnWidth
                        )
                        endDateAlert(
                            endDate = uiState.dateBoundary.endInclusive,
                        )
                    } else {
                        if (uiState.schedules.none { it.date.dayOfWeek.ordinal == page }) {
                            emptySchedule()
                            endDateAlert(
                                endDate = uiState.dateBoundary.endInclusive,
                            )
                        } else {
                            unfilteredClassesList(
                                classes = uiState.schedules.filter {
                                    it.date.dayOfWeek.ordinal == page
                                },
                                maxWidthOfDayAndWeek = maxWidthOfDayAndWeek,
                                timeColumnWidth = timeColumnWidth
                            )
                            endDateAlert(
                                endDate = uiState.dateBoundary.endInclusive,
                            )
                        }
                    }
                }
            }
        }

        val extraPadding = if (context.screenRotation == ScreenRotation.LEFT) {
            WindowInsets.navigationBars.asPaddingValues().end
        } else 0.dp

        if (uiState.filterByDays) {
            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                pageSpacing = AppTheme.spacing.l,
                modifier = Modifier
                    .fillMaxSize()
                    .thenIf(isLandScape) {
                       displayCutoutPadding()
                    }
            ) { page ->
                classesListContent(Modifier.padding(end = extraPadding), page)
            }
        } else {
            classesListContent(
                Modifier
                    .fillMaxSize()
                    .thenIf(isLandScape) {
                        displayCutoutPadding()
                    }
                    .padding(end = extraPadding),
                null
            )
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
    classes: List<ProfessorClass>,
    maxWidthOfDayAndWeek: Dp,
    timeColumnWidth: Dp
) {
    var currentMonth = 0

    classes.groupBy { it.date }.forEach { (date, lessons) ->
        if (currentMonth < date.monthNumber) {
            item {
                Headline(
                    text = remember(date) {
                        date.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
                            .uppercase()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = maxWidthOfDayAndWeek + AppTheme.spacing.l)
                        .padding(
                            top = AppTheme.spacing.xl,
                            bottom = AppTheme.spacing.l
                        )
                )
            }
            currentMonth = date.monthNumber
        } else {
            item {
                Spacer(Modifier.height(24.dp))
            }
        }

        teacherClassesGroup(
            classes = lessons,
            date = date,
            maxWidthOfDayAndWeek = maxWidthOfDayAndWeek,
            timeColumnWidth = timeColumnWidth,
        )
    }
}

@Composable
private fun ProfessorDetailsTopBar(
    uiState: ProfessorDetailsUiState,
    onIntent: (ProfessorDetailsIntent) -> Unit,
    onPageSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .then(
                with(Modifier) {
                    if (SDK_INT < 28) {
                        roundRectShadow(
                            offset = DpOffset(0.dp, 2.dp),
                            radius = 10.dp,
                        )
                    } else {
                        boxShadow(
                            color = SpotCard,
                            blurRadius = 6.dp,
                            offset = DpOffset(0.dp, 2.dp),
                            clip = false,
                            alpha = .5f
                        )
                    }
                }
            )
            .background(AppTheme.colorScheme.background3)
            .statusBarsPadding()
            .displayCutoutPadding()
            .thenIf(context.screenRotation == ScreenRotation.LEFT) {
                navigationBarsPadding()
            }
            .padding(bottom = AppTheme.spacing.s)
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
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
                AppBackButton(
                    onClick = { onIntent(ProfessorDetailsIntent.Back) },
                )
            },
            actions = {
                AppTextButton(
                    text = stringResource(
                        if (uiState.filterByDays) {
                            R.string.not_filter
                        } else R.string.filter_by_days
                    ),
                    onClick = {
                        onIntent(ProfessorDetailsIntent.ChangeFilter(!uiState.filterByDays))
                    }
                )
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.l)
                .background(AppTheme.colorScheme.background2, AppTheme.shapes.default)
                .padding(vertical = AppTheme.spacing.xs, horizontal = AppTheme.spacing.xs)
        ) {
            TabRow(
                selectedTabIndex = uiState.selectedPage,
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    androidx.compose.animation.AnimatedVisibility(
                        visible = uiState.filterByDays,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[uiState.selectedPage])
                            .zIndex(-1f)

                    ) {
                        val indicatorShape = AppTheme.shapes.default

                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .then(
                                    with(Modifier) {
                                        if (SDK_INT < 28) {
                                            roundRectShadow(
                                                offset = DpOffset(0.dp, 2.dp),
                                                shape = indicatorShape,
                                                radius = 10.dp,
                                            )
                                        } else {
                                            boxShadow(
                                                color = SpotCard,
                                                blurRadius = 6.dp,
                                                spreadRadius = 0.dp,
                                                offset = DpOffset(0.dp, 2.dp),
                                                shape = indicatorShape,
                                                clip = false,
                                                inset = false,
                                                alpha = .5f
                                            )
                                        }
                                    }
                                )
                                .background(AppTheme.colorScheme.background1, indicatorShape)
                        )
                    }
                },
                divider = {},
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                DayOfWeek.entries.filter { it != DayOfWeek.SUNDAY }.forEach { dayOfWeek ->
                    val selected = dayOfWeek.ordinal == uiState.selectedPage

                    AppRippleTheme(null) {
                        Surface(
                            color = Color.Transparent,
                            shape = AppTheme.shapes.default,
                            contentColor = if (selected) {
                                AppTheme.colorScheme.foregroundOnBrand
                            } else AppTheme.colorScheme.foreground,
                            onClick = {
                                if (selected) {
                                    ProfessorDetailsIntent.ChangeFilter(!uiState.filterByDays)
                                } else {
                                    onPageSelect(dayOfWeek.ordinal)
                                    ProfessorDetailsIntent.ChangePage(dayOfWeek.ordinal)
                                }.also(onIntent)
                            },
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(
                                text = remember {
                                    dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())
                                        .uppercase()
                                },
                                color = AppTheme.colorScheme.foreground,
                                style = AppTheme.typography.calloutButton,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                modifier = Modifier
                                    .width(IntrinsicSize.Max)
                                    .padding(
                                        vertical = 8.dp,
                                        horizontal = 12.dp
                                    )
                            )
                        }
                    }

                }
            }
        }
    }
}

private fun LazyListScope.emptySchedule() {
    item {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.spacing.xxxl),
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
                textAlign = TextAlign.Center,
                color = AppTheme.colorScheme.foreground3
            )
        }
    }
}

private fun LazyListScope.endDateAlert(
    endDate: LocalDate,
) {
    item {
        val formatedEndDate = remember(endDate) {
            DateTimeUtil.formatDate(endDate)
        }

        Text(
            text = stringResource(R.string.no_classes_until, formatedEndDate),
            style = AppTheme.typography.callout,
            color = AppTheme.colorScheme.foreground2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}