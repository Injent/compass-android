package ru.bgitu.feature.groups.presentation.groups

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppSecondaryButton
import ru.bgitu.core.designsystem.components.AppSnackbarHost
import ru.bgitu.core.designsystem.components.AppSwitch
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.designsystem.theme.start
import ru.bgitu.core.model.Group
import ru.bgitu.core.navigation.BackResultEffect
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.back
import ru.bgitu.core.navigation.push
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.groups.R
import ru.bgitu.feature.groups.model.GroupParcelable
import ru.bgitu.feature.groups.model.toExternalModel
import ru.bgitu.feature.groups.presentation.component.DeleteGroupDialog
import ru.bgitu.feature.groups.presentation.component.PrimaryGroupItem
import ru.bgitu.feature.groups.presentation.component.ReorderableGroupTabs
import kotlin.time.Duration.Companion.seconds

internal const val RESULT_PRIMARY_GROUP = "result_primary_group"
internal const val RESULT_SECONDARY_GROUP = "result_secondary_group"

@Composable
fun GroupsRoute() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val snackbarController = LocalSnackbarController.current
    val viewModel: GroupsViewModel = koinViewModel()

    viewModel.events.listenEvents { event ->
        when (event) {
            GroupsEvent.GroupNotSelectedAlert -> snackbarController.show(
                duration = 3.seconds,
                message = context.getString(R.string.alert_group_not_selected),
                withDismissAction = false,
                icon = AppIcons.WarningRed
            )
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackResultEffect {
        onResult<GroupParcelable>(RESULT_PRIMARY_GROUP) {
            viewModel.onIntent(GroupsIntent.ChangePrimaryGroup(it.toExternalModel()))
        }
        onResult<GroupParcelable>(RESULT_SECONDARY_GROUP) {
            viewModel.onIntent(GroupsIntent.AddGroup(it.toExternalModel()))
        }
    }

    var autoSearchWasProposed by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        (uiState as? GroupsUiState.Success)?.let { uiState ->
            if (uiState.primaryGroup == null && !autoSearchWasProposed) {
                autoSearchWasProposed = true
                navController.push(Screen.GroupSearch(resultKey = RESULT_PRIMARY_GROUP))
            }
        }
    }

    GroupsScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onBack = { navController.back() },
        onPrimaryGroupClick = {
            navController.push(Screen.GroupSearch(resultKey = RESULT_PRIMARY_GROUP))
        },
        onAddGroupRequest = {
            navController.push(Screen.GroupSearch(resultKey = RESULT_SECONDARY_GROUP))
        }
    )
}

@Composable
private fun GroupsScreen(
    uiState: GroupsUiState,
    onIntent: (GroupsIntent) -> Unit,
    onBack: () -> Unit,
    onPrimaryGroupClick: () -> Unit,
    onAddGroupRequest: () -> Unit,
) {
    val context = LocalContext.current
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    var groupInDialog by remember {
        mutableStateOf<Group?>(null)
    }

    groupInDialog?.let { group ->
        DeleteGroupDialog(
            group = group,
            onConfirm = {
                onIntent(GroupsIntent.RemoveGroup(group))
                groupInDialog = null
            },
            onDismiss = {
                groupInDialog = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_saved_groups),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1
                    )
                },
                navigationIcon = {
                    AppBackButton(onClick = onBack)
                }
            )
        },
        snackbarHost = {
            AppSnackbarHost(
                modifier = Modifier
                    .padding(bottom = AppTheme.spacing.l)
            )
        },
        modifier = Modifier
            .padding(start = LocalExternalPadding.current.start),
    ) { innerPadding ->
        val containerModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(
                start = AppTheme.spacing.l,
                end = AppTheme.spacing.l,
                bottom = AppTheme.spacing.l,
            )

        val leftContent: @Composable ColumnScope.() -> Unit = {
            when (uiState) {
                is GroupsUiState.Success -> {
                    PrimaryGroupItem(
                        groupName = uiState.primaryGroup?.name
                            ?: stringResource(R.string.group_not_selected),
                        onClick = onPrimaryGroupClick,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(AppTheme.spacing.l))

                    GroupsVisibilitySetting(
                        enabled = uiState.primaryGroup != null,
                        visible = uiState.showGroupsOnMainScreen,
                        onClick = {
                            onIntent(GroupsIntent.SetGroupVisibility(!uiState.showGroupsOnMainScreen))
                        }
                    )

                    if (isLandscape) {
                        Spacer(Modifier.weight(1f))

                        Text(
                            text = stringResource(R.string.youCantAddGroupsInLandscape),
                            style = AppTheme.typography.footnote,
                            color = AppTheme.colorScheme.foreground3,
                            modifier = Modifier.padding()
                        )
                    }
                }
                else -> {}
            }
        }

        val rightContent: @Composable ColumnScope.() -> Unit = {
            when (uiState) {
                is GroupsUiState.Success -> {
                    ReorderableGroupTabs(
                        groups = uiState.savedGroups,
                        onChange = {
                            onIntent(GroupsIntent.SetGroups(it))
                        },
                        onGroupClick = { groupInDialog = it },
                        enabled = uiState.showGroupsOnMainScreen && uiState.primaryGroup != null,
                        showHint = !isLandscape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    if (!isLandscape) {
                        Text(
                            text = stringResource(R.string.hint_groups),
                            style = AppTheme.typography.footnote,
                            color = AppTheme.colorScheme.foreground3,
                            modifier = Modifier.padding(vertical = AppTheme.spacing.s)
                        )

                        AppSecondaryButton(
                            text = stringResource(R.string.add_group),
                            onClick = onAddGroupRequest,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                else -> {}
            }
        }

        if (isLandscape) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
                modifier = containerModifier.zIndex(3f)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) { leftContent() }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .offset(y = -AppTheme.spacing.xs)
                ) { rightContent() }
            }
        } else {
            Column(
                modifier = containerModifier
            ) {
                leftContent()
                Spacer(Modifier.height(AppTheme.spacing.l))
                rightContent()
            }
        }

//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(AppTheme.spacing.l)
//        ) {
//            when (uiState) {
//                GroupsUiState.Loading -> {}
//                is GroupsUiState.Success -> {
//                    PrimaryGroupItem(
//                        groupName = uiState.primaryGroup?.name
//                            ?: stringResource(R.string.group_not_selected),
//                        onClick = onPrimaryGroupClick,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    Spacer(modifier = Modifier.height(AppTheme.spacing.l))
//
//                    GroupsVisibilitySetting(
//                        enabled = uiState.primaryGroup != null,
//                        visible = uiState.showGroupsOnMainScreen,
//                        onClick = {
//                            onIntent(GroupsIntent.SetGroupVisibility(!uiState.showGroupsOnMainScreen))
//                        }
//                    )
//
//                    Spacer(modifier = Modifier.height(AppTheme.spacing.l))
//
//                    ReorderableGroupTabs(
//                        groups = uiState.savedGroups,
//                        onChange = {
//                            onIntent(GroupsIntent.SetGroups(it))
//                        },
//                        onGroupClick = { groupInDialog = it },
//                        enabled = uiState.showGroupsOnMainScreen && uiState.primaryGroup != null,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1f)
//                    )
//
//                    if (uiState.savedGroups.size > 1) {
//                        Text(
//                            text = stringResource(R.string.hint_groups),
//                            style = AppTheme.typography.footnote,
//                            color = AppTheme.colorScheme.foreground3,
//                            modifier = Modifier.padding(top = AppTheme.spacing.s)
//                        )
//                    }
//
//                    Spacer(Modifier.height(AppTheme.spacing.l))
//
//                    AppSecondaryButton(
//                        text = stringResource(R.string.add_group),
//                        onClick = onAddGroupRequest,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    )
//                }
//                GroupsUiState.Error -> {}
//            }
//      }
    }
}

@Composable
private fun GroupsVisibilitySetting(
    enabled: Boolean,
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        modifier = modifier,
        onClick = onClick,
        shape = AppTheme.shapes.large
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.option_groups_visibility),
                style = AppTheme.typography.headline2,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = AppTheme.spacing.l)
            )
            AppSwitch(
                enabled = enabled,
                checked = visible,
                onCheckedChange = { onClick() }
            )
        }
    }
}