package ru.bgitu.app.presentation

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.radusalagean.infobarcompose.InfoBar
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.bgitu.app.R
import ru.bgitu.app.navigation.AppNavigator
import ru.bgitu.app.navigation.topLevelRoutes
import ru.bgitu.app.presentation.components.AppUpdateBottomSheetSetup
import ru.bgitu.app.presentation.components.GroupTabsInNavigation
import ru.bgitu.app.presentation.components.NavigationItems
import ru.bgitu.app.presentation.components.NotificationDialogEffect
import ru.bgitu.core.common.ScreenRotation
import ru.bgitu.core.common.eventbus.EventBus
import ru.bgitu.core.common.screenRotation
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.designsystem.components.AppBottomBarTokens
import ru.bgitu.core.designsystem.components.AppBottomNavigation
import ru.bgitu.core.designsystem.components.AppRailNavigation
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.components.rememberSnackController
import ru.bgitu.core.designsystem.components.rememberSnackbarController
import ru.bgitu.core.designsystem.components.snackbarContent
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.designsystem.theme.start
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.model.Group
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.NavigationAction
import ru.bgitu.core.navigation.Navigator
import ru.bgitu.core.navigation.ObserveAsEvents
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.push
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.home.impl.presentation.ChangeGroupView
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

    ObserveAsEvents(flow = Navigator.navigationActions) { action ->
        when (action) {
            is NavigationAction.Navigate -> navHostController.navigate(
                action.destination
            ) { action.navOptions(this) }
            NavigationAction.NavigateUp -> navHostController.navigateUp()
        }
    }

    val darkTheme = shouldUseDarkTheme(uiState)

    CompassTheme(
        darkTheme = darkTheme,
        dynamicColorAvailable = dynamicColorAvailable
    ) {
        if (SDK_INT >= 33) {
            NotificationDialogEffect()
        }
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
                uiState = uiState,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun AppMainScreenContent(
    navHostController: NavHostController,
    uiState: MainActivityUiState,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val snackController = rememberSnackController()
    val snackbarMessage by snackController.message.collectAsStateWithLifecycle()
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val isCompact = windowAdaptiveInfo.windowSizeClass
        .widthSizeClass <= WindowWidthSizeClass.Compact

    val context = LocalContext.current
    var railBarWidth by remember { mutableStateOf(0.dp) }

    val settings = koinInject<SettingsRepository>()
    val unseenFeatures by settings.unseenFeatures.collectAsStateWithLifecycle(emptyList())
    val primaryGroup by settings.data.map { it.primaryGroup }.collectAsStateWithLifecycle(null)
    val groups by settings.data.map { it.userPrefs.savedGroups }
        .collectAsStateWithLifecycle(emptyList())

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            InfoBar(
                offeredMessage = snackbarMessage,
                content = { message -> snackbarContent(message) { snackController.dismiss() } }
            ) { snackController.dismiss() }
        },
        modifier = Modifier
            .focusable() // For older Android devices where there is always a focused element required
    ) { innerPadding ->
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
            val showGroupTabs = remember { MutableTransitionState(true) }

            LaunchedEffect(navHostController) {
                navHostController.addOnDestinationChangedListener { _, destination, _ ->
                    val compact = windowAdaptiveInfo.windowSizeClass.widthSizeClass <= WindowWidthSizeClass.Medium

                    visibleState.targetState = destination.isTopLevelDestination() || !compact
                    showGroupTabs.targetState = destination.hasRoute(Screen.Home::class)
                }
            }

            var selectedGroup by rememberSaveable(
                saver = Saver(
                    save = { state ->
                        Bundle().apply {
                            state.value?.let {
                                putInt("id", it.id)
                                putString("name", it.name)
                            }
                        }
                    },
                    restore = { bundle ->
                        val name = bundle.getString("name") ?: return@Saver mutableStateOf(null)

                        mutableStateOf(
                            Group(
                                id = bundle.getInt("id"),
                                name = name
                            )
                        )
                    }
                )
            ) { mutableStateOf(primaryGroup) }

            LaunchedEffect(primaryGroup) {
                if (selectedGroup != null || primaryGroup == null) return@LaunchedEffect
                selectedGroup = primaryGroup
            }

            LaunchedEffect(Unit) {
                EventBus.subscribe<ChangeGroupView> { event ->
                    selectedGroup = event.group
                }
            }

            AnimatedVisibility(
                visibleState = visibleState,
                enter = if (isCompact) {
                    slideInVertically { it }
                } else slideInHorizontally { -it },
                exit = if (isCompact) {
                    slideOutVertically { it }
                } else slideOutHorizontally { -it },
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
                            unseenFeatures = unseenFeatures,
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
                                .width(180.dp)
                        ) {
                            NavigationItems(
                                avatarUrl = uiState.avatarUrl,
                                isCompact = false,
                                unseenFeatures = unseenFeatures,
                                modifier = Modifier
                            )

                            Spacer(Modifier.height(AppTheme.spacing.s))

                            val anX = with(density) {
                                if (context.screenRotation == ScreenRotation.RIGHT) {
                                    WindowInsets.navigationBars.asPaddingValues().start.roundToPx()
                                } else {
                                    WindowInsets.displayCutout.asPaddingValues().start.roundToPx()
                                }
                            }

                            AnimatedVisibility(
                                visibleState = showGroupTabs,
                                enter = slideInHorizontally { -(it + anX) },
                                exit = slideOutHorizontally { -(it + anX) },
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                GroupTabsInNavigation(
                                    groups = remember(primaryGroup, groups) {
                                        {
                                            if (primaryGroup != null) {
                                                mutableListOf(primaryGroup!!).apply {
                                                    addAll(groups)
                                                    toList()
                                                }
                                            } else groups
                                        }
                                    },
                                    selectedGroup = selectedGroup,
                                    onGroupSelected = { group ->
                                        scope.launch(NonCancellable) {
                                            EventBus.post(ChangeGroupView(group))
                                        }
                                    },
                                    onGroupSettingsClick = {
                                        navHostController.push(Screen.Groups)
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(
                                Modifier
                                    .navigationBarsPadding()
                                    .padding(bottom = AppTheme.spacing.l)
                            )
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