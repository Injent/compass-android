package ru.bgitu.app.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.bgitu.app.R
import ru.bgitu.app.navigation.AppNavigator
import ru.bgitu.app.navigation.topLevelRoutes
import ru.bgitu.app.presentation.components.AppUpdateBottomSheetSetup
import ru.bgitu.app.presentation.components.NavigationItems
import ru.bgitu.app.presentation.components.NavigationListenerEffect
import ru.bgitu.components.signin.model.AuthState
import ru.bgitu.core.common.ScreenRotation
import ru.bgitu.core.common.screenRotation
import ru.bgitu.core.designsystem.components.AppBottomBarTokens
import ru.bgitu.core.designsystem.components.AppBottomNavigation
import ru.bgitu.core.designsystem.components.AppRailNavigation
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.components.rememberSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.designsystem.theme.start
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.getId
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.update.presentation.DownloadProgressSnackbarEffect

@Composable
fun App(
    uiState: MainActivityUiState,
    viewModel: MainViewModel,
    dynamicColorAvailable: Boolean,
) {
    val context = LocalContext.current
    val snackbarController = rememberSnackbarController()
    val navHostController = rememberNavController()

    val darkTheme = shouldUseDarkTheme(uiState)

    CompassTheme(
        darkTheme = darkTheme,
        dynamicColorAvailable = dynamicColorAvailable
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
//                if (uiState.shouldShowOnboarding) {
//                    navHostController.navigate(Screen.Onboarding) {
//                        popUpTo<Screen.Loading> {
//                            inclusive = true
//                        }
//                        launchSingleTop = true
//                    }
//                    return@LaunchedEffect
//                }
                when (uiState.authState) {
                    AuthState.AUTHED, AuthState.ANONYMOUS -> {
                        navHostController.navigate(Screen.MainGraph) {
                            popUpTo<Screen.Loading> {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                    AuthState.NOT_AUTHED -> navHostController.navigate(Screen.Login()) {
                        popUpTo<Screen.Loading> {
                            inclusive = true
                        }
                        launchSingleTop = true
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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun AppMainScreenContent(
    navHostController: NavHostController,
    uiState: MainActivityUiState
) {
    val density = LocalDensity.current
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val isCompact = windowAdaptiveInfo.windowSizeClass
        .widthSizeClass <= WindowWidthSizeClass.Compact

    val context = LocalContext.current
    var railBarWidth by remember { mutableStateOf(0.dp) }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        NavigationListenerEffect(navController = navHostController)

        Box {
            CompositionLocalProvider(
                LocalExternalPadding provides PaddingValues(
                    start = railBarWidth,
                    bottom = if (isCompact) AppBottomBarTokens.Height else 0.dp
                )
            ) {
                AppNavigator(
                    navHostController = navHostController,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            val visibleState = remember { MutableTransitionState(true) }

            LaunchedEffect(navHostController) {
                navHostController.addOnDestinationChangedListener { _, destination, _ ->
                    visibleState.targetState = destination.isTopLevelDestination()
                }
            }

            AnimatedVisibility(
                visibleState = visibleState,
                enter = if (isCompact) {
                    slideInVertically { it }
                } else slideInHorizontally { it },
                exit = if (isCompact) {
                    slideOutVertically { it }
                } else slideOutHorizontally { it },
                modifier = Modifier
                    .align(
                        if (windowAdaptiveInfo.windowSizeClass
                                .widthSizeClass <= WindowWidthSizeClass.Compact) {
                            Alignment.BottomStart
                        } else Alignment.TopStart
                    )
            ) {

                if (isCompact) {
                    AppBottomNavigation(
                        modifier = Modifier
                            .zIndex(1f)
                            .navigationBarsPadding()
                    ) {
                        NavigationItems(
                            avatarUrl = uiState.avatarUrl,
                            isCompact = true,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                } else {
                    val displayCutout = WindowInsets.displayCutout.asPaddingValues().start
                    Box(
                        modifier = Modifier
                            .background(AppTheme.colorScheme.background3)
                            .zIndex(1f)
                            .thenIf(context.screenRotation == ScreenRotation.LEFT) {
                                displayCutoutPadding()
                            }
                            .thenIf(context.screenRotation == ScreenRotation.RIGHT) {
                                navigationBarsPadding()
                            }
                            .onPlaced { coors ->
                                railBarWidth = density.run {
                                    coors.size.width.toDp() +
                                            if (context.screenRotation == ScreenRotation.LEFT) {
                                                displayCutout
                                            } else 0.dp
                                }
                            }
                    ) {
                        AppRailNavigation(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                        ) {
                            NavigationItems(
                                avatarUrl = uiState.avatarUrl,
                                isCompact = false,
                                spacer = {
                                    Spacer(Modifier.weight(1f))
                                },
                                modifier = Modifier
                            )
                            Spacer(Modifier.navigationBarsPadding().padding(bottom = AppTheme.spacing.l))
                        }
                    }
                }
            }
        }
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
    return topLevelRoutes.any {
        this?.hasRoute(it.route::class) ?: false
    }
}