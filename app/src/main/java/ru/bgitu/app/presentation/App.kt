package ru.bgitu.app.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.bgitu.app.R
import ru.bgitu.app.navigation.AppNavigator
import ru.bgitu.app.presentation.components.AppUpdateBottomSheetSetup
import ru.bgitu.app.presentation.components.NavigationItems
import ru.bgitu.app.presentation.components.NavigationListenerEffect
import ru.bgitu.core.data.model.AuthStatus
import ru.bgitu.core.designsystem.components.AppBottomNavigation
import ru.bgitu.core.designsystem.components.AppSnackbarHost
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.components.rememberSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.getId
import ru.bgitu.core.navigation.replaceAll
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.update.presentation.DownloadProgressSnackbarEffect

@Composable
fun App(
    uiState: MainActivityUiState,
    viewModel: MainViewModel,
    dynamicColorAllowed: Boolean,
) {
    val context = LocalContext.current
    val snackbarController = rememberSnackbarController()
    val navHostController = rememberNavController()

    val darkTheme = shouldUseDarkTheme(uiState)

    CompassTheme(
        darkTheme = darkTheme,
        dynamicColorAllowed = dynamicColorAllowed
    ) {
        AppUpdateBottomSheetSetup(uiState = uiState)

        CompositionLocalProvider(
            LocalSnackbarController provides snackbarController,
            LocalNavController provides navHostController
        ) {
            viewModel.events.listenEvents { event ->
                when (event) {
                    is MainActivityEvent.ShowErrorSnackbar -> {
                        snackbarController.dismiss()
                        snackbarController.show(
                            message = event.details.asString(context),
                            withDismissAction = true,
                            icon = AppIcons.WarningRed,
                            actionLabel = context.getString(R.string.retry),
                            onAction = {
                                viewModel.onIntent(MainActivityIntent.RetryDownload)
                            }
                        )
                    }
                    MainActivityEvent.HideSnackBar -> {
                        snackbarController.dismiss()
                    }
                }
            }

            LaunchedEffect(uiState) {
                if (navHostController.currentDestination?.id != getId<Screen.Loading>()) {
                    return@LaunchedEffect
                }
                when (uiState.authStatus) {
                    AuthStatus.SIGNED, AuthStatus.SIGNED_AS_GUEST -> {
                        navHostController.clearBackStack<Screen.Loading>()
                        navHostController.replaceAll(Screen.MainGraph)
                    }
                    AuthStatus.SIGNIN_REQUIRED -> {
                        navHostController.replaceAll(Screen.LoginGraph)
                    }
                    else -> Unit
                }
            }

            DownloadProgressSnackbarEffect(
                installState = uiState.installState,
                onUpdateRequest = {
                    viewModel.onIntent(MainActivityIntent.InstallUpdate)
                },
                onRetryRequest = {
                    viewModel.onIntent(MainActivityIntent.RetryDownload)
                }
            )

            AppMainScreenContent(
                navHostController = navHostController,
                uiState = uiState
            )
        }
    }
}

@Composable
private fun AppMainScreenContent(
    navHostController: NavHostController,
    uiState: MainActivityUiState
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            val visibleState = remember { MutableTransitionState(false) }

            LaunchedEffect(navHostController) {
                navHostController.addOnDestinationChangedListener { _, destination, _ ->
                    visibleState.targetState = destination.isTopLevelDestination()
                }
            }

            AnimatedVisibility(
                visibleState = visibleState,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    AppBottomNavigation {
                        NavigationItems(
                            avatarUrl = uiState.avatarUrl,
                            itemModifier = Modifier
                                .weight(1f)
                        )
                    }

                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .background(AppTheme.colorScheme.background3)
                            .navigationBarsPadding()
                    )
                }
            }
        },
        snackbarHost = {
            AppSnackbarHost(
                Modifier.imePadding()
            )
        }
    ) { innerPadding ->
        NavigationListenerEffect(navController = navHostController)

        AppNavigator(
            navHostController = navHostController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
private fun shouldUseDarkTheme(uiState: MainActivityUiState): Boolean {
    return if (uiState.uiTheme == UiTheme.SYSTEM) {
        isSystemInDarkTheme()
    } else {
        uiState.uiTheme == UiTheme.DARK
    }
}

fun NavDestination?.isTopLevelDestination(): Boolean {
    return listOf(
        getId<Screen.Home>(),
        getId<Screen.ProfessorSearch>(),
        getId<Screen.SearchMate>(),
        getId<Screen.Profile>()
    ).contains(this?.id)
}