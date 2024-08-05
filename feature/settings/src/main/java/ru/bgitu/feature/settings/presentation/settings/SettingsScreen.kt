package ru.bgitu.feature.settings.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppSnackbarHost
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.model.settings.UserPrefs
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.back
import ru.bgitu.core.navigation.push
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.settings.R
import ru.bgitu.feature.settings.presentation.components.AppThemeView
import ru.bgitu.feature.settings.presentation.components.ScheduleNotificationView

@Composable
fun SettingsScreen() {
    val navController = LocalNavController.current
    val viewModel: SettingsViewModel = koinViewModel()

    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    viewModel.events.listenEvents { event ->
        when (event) {
            SettingsEvent.NavigateBack -> navController.back()
            SettingsEvent.NavigateToAbout -> navController.push(Screen.About)
            SettingsEvent.NavigateToHelp -> navController.push(Screen.Help)
        }
    }

    (settingsUiState as? SettingsUiState.Success)?.let { state ->
        SettingsScreenContent(
            uiState = state,
            onIntent = viewModel::onIntent,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
    uiState: SettingsUiState.Success,
    onIntent: (SettingsIntent) -> Unit,
) {


    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1
                    )
                },
                navigationIcon = {
                    AppBackButton(
                        onClick = { onIntent(SettingsIntent.NavigateBack) },
                    )
                }
            )
        },
        snackbarHost = { AppSnackbarHost() }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = AppTheme.spacing.xl)
                .padding(top = AppTheme.spacing.xl)
                .verticalScroll(scrollState),
        ) {
            AppThemeView(
                currentTheme = uiState.prefs.theme,
                onChangeTheme = { theme ->
                    onIntent(SettingsIntent.ChangeUiTheme(theme))
                }
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.colorScheme.background1
            )
            ScheduleNotificationView(
                enabled = uiState.prefs.showPinnedSchedule,
                onSwitch = { enabled ->
                    onIntent(SettingsIntent.SwitchScheduleNotifier(enabled))
                }
            )
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    CompassTheme {
        SettingsScreenContent(
            uiState = SettingsUiState.Success(
                prefs = UserPrefs(
                    theme = UiTheme.SYSTEM,
                    showPinnedSchedule = false,
                    ignoreMinorUpdates = false,
                    teacherSortByWeeks = false,
                    savedGroups = emptyList(),
                    showGroupsOnMainScreen = true
                )
            ),
            onIntent = {},
        )
    }
}