package ru.bgitu.feature.findmate.presentation.search

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.bgitu.core.designsystem.components.AppSearchField
import ru.bgitu.core.designsystem.illustration.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.designsystem.theme.bottom
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.feature.findmate.R
import ru.bgitu.feature.findmate.presentation.components.MateView

@Composable
fun SearchMateRoute(
    viewModel: SearchMateViewModel
) {
    val navController = LocalNavController.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchMateScreen(
        uiState = uiState,
        searchField = viewModel.searchField
    )
}

@Composable
private fun SearchMateScreen(
    uiState: SearchMateUiState,
    searchField: TextFieldState,
) {
    Scaffold(
        topBar = {
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.spacing.l,
                        end = AppTheme.spacing.l,
                        top = AppTheme.spacing.l,
                        bottom = AppTheme.spacing.xs
                    )
            ) {
                Text(
                    text = stringResource(R.string.title_search_mate),
                    style = AppTheme.typography.title3,
                    color = AppTheme.colorScheme.foreground1
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppSearchField(
                        state = searchField,
                        placeholder = stringResource(R.string.search_subject),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        modifier = Modifier
            .systemBarsPadding()
            .padding(bottom = LocalExternalPadding.current.bottom)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchResults(uiState = uiState)
        }
    }
}

@Composable
private fun IllustrationWithText(
    @DrawableRes illustation: Int,
    headline: String,
    details: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(AppTheme.spacing.xl),
    ) {
        Image(
            painter = painterResource(illustation),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        )
        Text(
            text = headline,
            style = AppTheme.typography.title3,
            color = AppTheme.colorScheme.foreground1
        )
        Text(
            text = details,
            style = AppTheme.typography.callout,
            color = AppTheme.colorScheme.foreground2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ColumnScope.SearchResults(uiState: SearchMateUiState) {
    when (uiState) {
        is SearchMateUiState.Error -> Unit
        SearchMateUiState.Idle -> {
            IllustrationWithText(
                illustation = AppIllustrations.SearchMate,
                headline = stringResource(R.string.search_mate),
                details = stringResource(R.string.help_each_other),
                modifier = Modifier.weight(1f)
            )
        }
        SearchMateUiState.NotFound -> {
            IllustrationWithText(
                illustation = AppIllustrations.NotFound,
                headline = stringResource(R.string.nobody_found),
                details = stringResource(R.string.nobody_found_details),
                modifier = Modifier.weight(1f)
            )
        }
        is SearchMateUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = uiState.profiles,
                    key = { it.userId }
                ) { item ->
                    MateView(mate = item)
                }
            }
        }
    }
}