package ru.bgitu.feature.professor_search.presentation.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.bgitu.core.common.CYRILLIC_REGEX
import ru.bgitu.core.designsystem.components.AppSearchField
import ru.bgitu.core.designsystem.components.InputRegex
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.ui.AppSearchItem
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.professor_search.R
import ru.bgitu.feature.professor_search.presentation.components.RecentProfessorSearch
import ru.bgitu.feature.professor_search.presentation.components.TeacherScheduleAlertDialog
import kotlin.time.Duration.Companion.seconds

@Composable
fun ProfessorSearchRoute(
    viewModel: ProfessorSearchViewModel,
    onNavigateToDetails: (professorName: String) -> Unit
) {
    val snackbarController = LocalSnackbarController.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchFieldState = viewModel.searchFieldState

    viewModel.events.listenEvents { event ->
        when (event) {
            is ProfessorSearchEvent.ShowError -> {
                snackbarController.show(
                    duration = 3.seconds,
                    message = event.errorDetails.asString(context),
                    withDismissAction = false,
                    icon = AppIcons.WarningRed,
                )
            }

            is ProfessorSearchEvent.NavigateToProfessorDetails -> {
                onNavigateToDetails(event.professorName)
            }
        }
    }

    if (!uiState.seenScheduleAlert) {
        TeacherScheduleAlertDialog(
            onConfirm = {
                viewModel.onIntent(ProfessorSearchIntent.SeenAlert)
            }
        )
    }

    ProfessorSearchScreen(
        uiState = uiState,
        searchFieldState = searchFieldState,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun ProfessorSearchScreen(
    uiState: ProfessorSearchUiState,
    searchFieldState: TextFieldState,
    onIntent: (ProfessorSearchIntent) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ProfessorSearchTopBar(searchFieldState = searchFieldState)
        },
        modifier = Modifier.statusBarsPadding()
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
                    onIntent(ProfessorSearchIntent.SelectProfessor(professorName))
                },
                onClear = {
                    onIntent(ProfessorSearchIntent.ClearSearch)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SearchResults(
    uiState: ProfessorSearchUiState,
    onSelect: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.searchResults.isNotEmpty() -> {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(1.2.dp),
                contentPadding = WindowInsets.ime.asPaddingValues()
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
        !uiState.isLoading -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
                    .padding(AppTheme.spacing.xl),
            ) {
                Image(
                    painter = painterResource(AppIllustrations.Teacher),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                )
                Text(
                    text = stringResource(R.string.search_professors_title),
                    style = AppTheme.typography.title3,
                    color = AppTheme.colorScheme.foreground1
                )
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
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
        modifier = Modifier.padding(AppTheme.spacing.l)
    ) {
        Text(
            text = stringResource(R.string.professor_search),
            style = AppTheme.typography.title3,
            color = AppTheme.colorScheme.foreground1,
        )
        AppSearchField(
            state = searchFieldState,
            inputTransformation = remember { InputRegex(CYRILLIC_REGEX) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ProfessorSearchScreenPreview() {
    CompassTheme {
        ProfessorSearchScreen(
            uiState = ProfessorSearchUiState(),
            searchFieldState = TextFieldState(),
            onIntent = {}
        )
    }
}