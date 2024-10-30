package ru.bgitu.feature.professor_search.presentation.search

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.common.CYRILLIC_REGEX
import ru.bgitu.core.designsystem.components.AppSearchField
import ru.bgitu.core.designsystem.components.AppSnackbarHost
import ru.bgitu.core.designsystem.components.CompassLoading
import ru.bgitu.core.designsystem.components.InputRegex
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.illustration.AppIllustrations
import ru.bgitu.core.designsystem.illustration.Proffessor
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.designsystem.theme.bottom
import ru.bgitu.core.designsystem.theme.start
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.ui.AppSearchItem
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.professor_search.R
import ru.bgitu.feature.professor_search.presentation.KEY_TEACHER_VIEWMODEL
import ru.bgitu.feature.professor_search.presentation.components.RecentProfessorSearch
import ru.bgitu.feature.professor_search.presentation.components.TeacherScheduleAlertDialog
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun TeacherSearchRoute(
    onNavigateToDetails: (teacherName: String) -> Unit,
) {
    val snackbarController = LocalSnackbarController.current
    val context = LocalContext.current
    val viewModel: TeacherSearchViewModel = koinViewModel(
        key = KEY_TEACHER_VIEWMODEL,
        viewModelStoreOwner = context as ComponentActivity
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchFieldState = viewModel.searchFieldState

    viewModel.events.listenEvents { event ->
        when (event) {
            is TeacherSearchEvent.ShowError -> {
                snackbarController.show(
                    duration = 3.seconds,
                    message = event.errorDetails.asString(context),
                    withDismissAction = false,
                    icon = AppIcons.WarningRed,
                )
            }

            is TeacherSearchEvent.NavigateToTeacherDetails -> {
                onNavigateToDetails(event.teacherName)
            }
        }
    }

    TeacherSearchScreen(
        uiState = uiState,
        searchFieldState = searchFieldState,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun TeacherSearchScreen(
    uiState: TeacherSearchUiState,
    searchFieldState: TextFieldState,
    onIntent: (TeacherSearchIntent) -> Unit
) {
    if (!uiState.seenScheduleAlert) {
        TeacherScheduleAlertDialog(
            onConfirm = {
                onIntent(TeacherSearchIntent.SeenAlert)
            }
        )
    }

    val imeVisible = WindowInsets.isImeVisible

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ProfessorSearchTopBar(searchFieldState = searchFieldState)
        },
        snackbarHost = {
            AppSnackbarHost(
                Modifier
                    .thenIf(imeVisible) {
                        offset(y = LocalExternalPadding.current.bottom)
                    }
                    .padding(bottom = AppTheme.spacing.l)
                    .imePadding()
            )
        },
        modifier = Modifier
            .systemBarsPadding()
            .padding(start = LocalExternalPadding.current.start)
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .padding(horizontal = AppTheme.spacing.l)
        ) {
            SearchResults(
                uiState = uiState,
                onSelect = { professorName ->
                    onIntent(TeacherSearchIntent.SelectTeacher(professorName))
                },
                onClear = {
                    onIntent(TeacherSearchIntent.ClearSearch)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .displayCutoutPadding()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun SearchResults(
    uiState: TeacherSearchUiState,
    onSelect: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    when {
        uiState.searchResults.isNotEmpty() -> {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(1.2.dp),
                contentPadding = PaddingValues(
                    bottom = (WindowInsets.ime.asPaddingValues().calculateBottomPadding()
                            - LocalExternalPadding.current.bottom
                            - WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    ).coerceAtLeast(0.dp)
                )
            ) {
                items(
                    items = uiState.searchResults,
                    key = { it.hashCode() },
                ) { professorName ->
                    AppSearchItem(
                        label = professorName,
                        onClick = { onSelect(professorName) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .thenIf(uiState.searchResults.first() == professorName) {
                                clip(AppTheme.shapes.defaultTopCarved)
                            }
                            .thenIf(uiState.searchResults.last() == professorName) {
                                clip(AppTheme.shapes.defaultBottomCarved)
                            }
                            .then(
                                if (uiState.searchResults.last() != professorName) {
                                    Modifier.padding(bottom = 1.5.dp)
                                } else Modifier.clip(AppTheme.shapes.defaultBottomCarved)
                            )
                    )
                }
                item {
                    Spacer(Modifier.height(AppTheme.spacing.l))
                }
            }
        }
        uiState.recentSearchResults.isNotEmpty() -> {
            RecentProfessorSearch(
                uiState = uiState,
                onClick = onSelect,
                onClear = onClear,
                modifier = modifier
            )
        }
        uiState.isLoading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CompassLoading()
            }
        }
        else -> {
            PlaceholderContent(
                windowAdaptiveInfo = windowAdaptiveInfo,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun PlaceholderContent(
    windowAdaptiveInfo: WindowAdaptiveInfo,
    modifier: Modifier = Modifier,
) {
    if (windowAdaptiveInfo.windowSizeClass.widthSizeClass <= WindowWidthSizeClass.Compact) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.spacing.xl),
        ) {
            Image(
                imageVector = AppIllustrations.Proffessor,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth(),
            )
            Text(
                text = stringResource(R.string.search_professors_title),
                style = AppTheme.typography.title3,
                textAlign = TextAlign.Center,
                color = AppTheme.colorScheme.foreground1
            )
            Text(
                text = stringResource(R.string.search_professors_description),
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
                imageVector = AppIllustrations.Proffessor,
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
                    text = stringResource(R.string.search_professors_title),
                    style = AppTheme.typography.title3,
                    textAlign = TextAlign.Center,
                    color = AppTheme.colorScheme.foreground1
                )
                Spacer(Modifier.height(AppTheme.spacing.s))
                Text(
                    text = stringResource(R.string.search_professors_description),
                    style = AppTheme.typography.callout,
                    color = AppTheme.colorScheme.foreground2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ProfessorSearchTopBar(
    searchFieldState: TextFieldState
) {
    Column(
        modifier = Modifier
    ) {
        TopAppBar(
            windowInsets = WindowInsets(0),
            title = {
                Text(
                    text = stringResource(R.string.professor_search),
                    style = AppTheme.typography.title3,
                    color = AppTheme.colorScheme.foreground1,
                )
            }
        )
        AppSearchField(
            state = searchFieldState,
            placeholder = stringResource(R.string.hint_enterTeacherLastName),
            inputTransformation = remember { InputRegex(CYRILLIC_REGEX) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.l)
        )
    }
}

@Preview
@Composable
private fun ProfessorSearchScreenPreview() {
    CompassTheme {
        TeacherSearchScreen(
            uiState = TeacherSearchUiState(),
            searchFieldState = TextFieldState(),
            onIntent = {}
        )
    }
}