package ru.bgitu.feature.settings.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.common.ScreenRotation
import ru.bgitu.core.common.screenRotation
import ru.bgitu.core.designsystem.components.AppSnackbarHost
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.designsystem.theme.start
import ru.bgitu.core.designsystem.util.shadow.basicShadow
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.back
import ru.bgitu.core.navigation.push
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.settings.R
import ru.bgitu.feature.settings.presentation.components.AppThemeView
import ru.bgitu.feature.settings.presentation.components.NotificationsGroup
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
            SettingsEvent.NavigateToGroups -> navController.push(Screen.Groups)
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
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .padding(start = LocalExternalPadding.current.start),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.background3
                ),
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
                },
                modifier = Modifier.basicShadow()
            )
        },
        snackbarHost = { AppSnackbarHost() },
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            modifier = Modifier
                .thenIf(context.screenRotation == ScreenRotation.RIGHT) {
                    displayCutoutPadding()
                }
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = AppTheme.spacing.xl),
        ) {
            Spacer(Modifier.height(AppTheme.spacing.xs))
            AppThemeView(
                uiState = uiState,
                onIntent = onIntent
            )

            ScheduleNotificationView(
                uiState = uiState,
                onGroupSelectRequest = {
                    onIntent(SettingsIntent.NavigateToGroups)
                },
                onSwitch = { enabled ->
                    onIntent(SettingsIntent.SwitchScheduleNotifier(enabled))
                }
            )

            NotificationsGroup(
                uiState = uiState,
                onIntent = onIntent,
            )

//            OtherSettingsGroup(
//                helpTraffic = uiState.prefs.helpSiteTraffic,
//                onSwitchTraffic = { enabled ->
//                    onIntent(SettingsIntent.SwitchHelpSiteTraffic(enabled))
//                }
//            )
            Spacer(Modifier.height(AppTheme.spacing.xl))
            Spacer(Modifier.padding(LocalExternalPadding.current))
        }
    }
}
