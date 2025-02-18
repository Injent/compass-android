package ru.bgitu.feature.notes.presentation.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.CheckCircle
import ru.bgitu.core.designsystem.icon.ProgressComplete
import ru.bgitu.core.designsystem.icon.Trash
import ru.bgitu.core.designsystem.illustration.AppIllustrations
import ru.bgitu.core.designsystem.illustration.Tasks
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.ui.Headline
import ru.bgitu.feature.notes.R
import ru.bgitu.feature.notes.model.ShortNote
import ru.bgitu.feature.notes.presentation.component.NoteItem

@Composable
fun NotesRoute(
    onNoteClick: (Int) -> Unit
) {
    val viewModel = koinViewModel<NotesViewModel>()

    val groupedNotes by viewModel.groupedNotes.collectAsStateWithLifecycle()
    val completedNotes by viewModel.completedNotes.collectAsStateWithLifecycle()
    val deletedNotes by viewModel.deletedNotes.collectAsStateWithLifecycle()
    val tabIndex by viewModel.selectedTabIndex.collectAsStateWithLifecycle()

    NotesScreen(
        selectedTabIndex = tabIndex,
        onNoteClick = onNoteClick,
        groupedNotes = groupedNotes,
        completedNotes = completedNotes,
        deletedNotes = deletedNotes,
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun NotesScreen(
    selectedTabIndex: Int,
    groupedNotes: Map<String, List<ShortNote>>,
    completedNotes: List<ShortNote>,
    deletedNotes: List<ShortNote>,
    onNoteClick: (Int) -> Unit,
    onIntent: (NotesIntent) -> Unit,
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 3 }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect {
            onIntent(NotesIntent.SelectTab(pagerState.currentPage))
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            NoteTopAppBar(
                selectedTabIndex = selectedTabIndex,
                onSelect = { tabIndex ->
                    coroutineScope.launch {
                        withContext(NonCancellable) {
                            pagerState.animateScrollToPage(tabIndex)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        @Composable
        fun PresetLazyColumn(page: Int, isEmpty: Boolean) {
            if (isEmpty) {
                NoTasksIllustration(windowAdaptiveInfo = windowAdaptiveInfo)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = AppTheme.spacing.l),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = AppTheme.spacing.l)
                ) {
                    when (page) {
                        0 -> uncompletedNotes(
                            coroutineScope = coroutineScope,
                            groupedNotes = groupedNotes,
                            onNoteClick = onNoteClick,
                            onChecked = { noteId, checked ->
                                onIntent(NotesIntent.SetCompleted(noteId, checked))
                            }
                        )
                        1 -> notes(
                            notes = completedNotes,
                            onNoteClick = onNoteClick,
                            onChecked = { noteId, checked ->
                                onIntent(NotesIntent.SetCompleted(noteId, checked))
                            }
                        )
                        2 -> notes(
                            notes = deletedNotes,
                            onNoteClick = onNoteClick,
                            onChecked = { noteId, checked ->
                                onIntent(NotesIntent.SetCompleted(noteId, checked))
                            }
                        )
                    }
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 0,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> AnimatedContent(
                    targetState = groupedNotes.isEmpty()
                ) { PresetLazyColumn(page = page, isEmpty = it) }
                1 -> AnimatedContent(
                    targetState = completedNotes.isEmpty()
                ) { PresetLazyColumn(page = page, isEmpty = it) }
                2 -> AnimatedContent(
                    targetState = deletedNotes.isEmpty()
                ) { PresetLazyColumn(page = page, isEmpty = it) }
            }
        }
    }
}

@Composable
fun NoteTopAppBar(
    selectedTabIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        TopAppBar(
            windowInsets = WindowInsets(0),
            title = {
                Text(
                    text = stringResource(R.string.title_homework),
                    style = AppTheme.typography.title3,
                    color = AppTheme.colorScheme.foreground1
                )
            }
        )

        Box(
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.l)
                .background(AppTheme.colorScheme.background4, AppTheme.shapes.default)
        ) {
            TabRow(
                containerColor = Color.Transparent,
                selectedTabIndex = selectedTabIndex,
                indicator = { tabPositions ->
                    AppCard(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .fillMaxHeight()
                            .zIndex(-1f),
                        content = {}
                    )
                },
                divider = {},
                modifier = Modifier.padding(AppTheme.spacing.xs)
            ) {
                AppRippleTheme(null) {
                    tabIcons.forEachIndexed { tabIndex, icon ->
                        Surface(
                            color = Color.Transparent,
                            onClick = {
                                onSelect(tabIndex)
                            },
                            shape = AppTheme.shapes.default,
                            modifier = Modifier
                                .height(40.dp)
                        ) {
                            val iconColor by animateColorAsState(
                                targetValue = if (tabIndex == selectedTabIndex) {
                                    AppTheme.colorScheme.foreground
                                } else AppTheme.colorScheme.foreground2
                            )
                            Text(
                                text = "В процессе",
                                color = iconColor,
                                style = AppTheme.typography.callout
                            )
//                            Icon(
//                                imageVector = icon,
//                                contentDescription = null,
//                                tint = iconColor,
//                                modifier = Modifier
//                                    .padding(vertical = 8.dp)
//                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun NoTasksIllustration(
    windowAdaptiveInfo: WindowAdaptiveInfo,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = AppTheme.spacing.xl, vertical = AppTheme.spacing.l),
    ) {
        val imageModifier = when (windowAdaptiveInfo.windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact, WindowWidthSizeClass.Expanded -> Modifier.height(200.dp)
            else -> Modifier.weight(1f)
        }
        Image(
            imageVector = AppIllustrations.Tasks,
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = imageModifier
                .fillMaxWidth(),
        )
        Text(
            text = stringResource(R.string.empty),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.title3,
            color = AppTheme.colorScheme.foreground1,
            modifier = Modifier
        )
        Text(
            text = stringResource(R.string.click_on_card_to_add_note),
            color = AppTheme.colorScheme.foreground2,
            style = AppTheme.typography.callout,
            textAlign = TextAlign.Center
        )
    }
}

private fun LazyListScope.uncompletedNotes(
    coroutineScope: CoroutineScope,
    groupedNotes: Map<String, List<ShortNote>>,
    onNoteClick: (Int) -> Unit,
    onChecked: (Int, Boolean) -> Unit,
) {
    groupedNotes.forEach { (subjectName, notes) ->
        item(key = subjectName) {
            Headline(
                text = subjectName,
                modifier = Modifier
                    .padding(bottom = AppTheme.spacing.s)
                    .animateItem()
            )
        }
        notes.forEach { note ->
            item(
                key = note.id
            ) {
                var markAsChecked by remember { mutableStateOf(false) }

                NoteItem(
                    shortText = note.shortName,
                    completed = note.completed || markAsChecked,
                    onClick = {
                        onNoteClick(note.id)
                    },
                    onChecked = {
                        markAsChecked = true
                        coroutineScope.launch {
                            withContext(NonCancellable) {
                                delay(1000)
                                onChecked(note.id, it)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                )
            }
        }
    }
}

private fun LazyListScope.notes(
    notes: List<ShortNote>,
    onNoteClick: (Int) -> Unit,
    onChecked: (Int, Boolean) -> Unit
) {
    items(
        items = notes,
        key = { it.id }
    ) { note ->
        NoteItem(
            shortText = note.shortName,
            completed = note.completed,
            onClick = {
                onNoteClick(note.id)
            },
            onChecked = { onChecked(note.id, it) },
            modifier = Modifier
                .fillMaxWidth()
                .animateItem()
        )
    }
}

private val tabIcons = arrayOf(
    AppIcons.ProgressComplete,
    AppIcons.CheckCircle,
    AppIcons.Trash
)