package ru.bgitu.feature.groups.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppSearchField
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.model.Group
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.AppSearchItem
import ru.bgitu.feature.groups.R
import ru.bgitu.feature.groups.model.GroupParcelable

@Composable
fun GroupSearchRoute(resultKey: String) {
    val navController = LocalNavController.current
    val viewModel: GroupSearchViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GroupSearchScreen(
        uiState = uiState,
        searchFieldState = viewModel.searchFieldState,
        onSelect = { group ->
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(resultKey, GroupParcelable(group.id, group.name))
            navController.back()
        },
        onBack = navController::back
    )
}

@Composable
private fun GroupSearchScreen(
    uiState: GroupSearchUiState,
    searchFieldState: TextFieldState,
    onSelect: (Group) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_find_group),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1
                    )
                },
                navigationIcon = {
                    AppBackButton(
                        onClick = onBack,
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    top = AppTheme.spacing.l,
                    start = AppTheme.spacing.l,
                    end = AppTheme.spacing.l,
                )
        ) {
            val focusRequester = remember { FocusRequester() }

            AppSearchField(
                state = searchFieldState,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { state ->
                        if (!state.isFocused) {
                            onBack()
                        }
                    }
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                itemsIndexed(
                    items = uiState.results,
                    key = { _, item -> item.id }
                ) { index, group ->
                    if (index != 0) {
                        HorizontalDivider(
                            thickness = AppTheme.strokeWidth.thick,
                            color = AppTheme.colorScheme.stroke2
                        )
                    }
                    AppSearchItem(
                        label = group.name,
                        onClick = { onSelect(group) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .thenIf(index == 0) {
                                clip(AppTheme.shapes.defaultTopCarved)
                            }
                            .thenIf(index == uiState.results.size - 1) {
                                clip(AppTheme.shapes.defaultBottomCarved)
                            }
                    )
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .imePadding()
                            .navigationBarsPadding()
                    )
                }
            }
        }
    }
}