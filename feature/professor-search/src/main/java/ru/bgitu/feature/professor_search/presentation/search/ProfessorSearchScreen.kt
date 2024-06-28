package ru.bgitu.feature.professor_search.presentation.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import ru.bgitu.core.common.CYRILLIC_REGEX
import ru.bgitu.core.designsystem.components.AppSearchField
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.components.InputRegex
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.util.ClearFocusWithImeEffect
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.ui.AppSearchItem
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.professor_search.R
import ru.bgitu.feature.professor_search.presentation.components.RecentProfessorSearch
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
        TeacherScheduleAlert(
            onConfirmation = {
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
    ClearFocusWithImeEffect()

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
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(AppIllustrations.Teacher),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.33f)
                        .heightIn(max = 300.dp)
                        .widthIn(max = 300.dp)
                )
                Spacer(Modifier.height(AppTheme.spacing.l))
                Text(
                    text = stringResource(R.string.you_didnt_search_professors),
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

@Composable
private fun TeacherScheduleAlert(
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmationDelay by remember { mutableIntStateOf(5) }

    LaunchedEffect(Unit) {
        while (confirmationDelay > 0) {
            delay(1.seconds)
            confirmationDelay--
        }
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {},
        confirmButton = {
            AppTextButton(
                text = stringResource(android.R.string.ok).let {
                    if (confirmationDelay > 0) {
                        "$it ($confirmationDelay)"
                    } else it
                },
                onClick = onConfirmation,
                enabled = confirmationDelay == 0
            )
        },
        containerColor = AppTheme.colorScheme.background1,
        shape = AppTheme.shapes.default,
        title = {
            Text(
                text = stringResource(R.string.attention),
                style = AppTheme.typography.title3,
                color = AppTheme.colorScheme.foreground1
            )
        },
        text = {
            Text(
                text = stringResource(R.string.alert_text),
                style = AppTheme.typography.body,
                color = AppTheme.colorScheme.foreground1
            )
        }
    )
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