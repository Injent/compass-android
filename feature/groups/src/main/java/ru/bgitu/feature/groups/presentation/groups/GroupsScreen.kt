package ru.bgitu.feature.groups.presentation.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppSwitch
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.ClearFocusWithImeEffect
import ru.bgitu.core.model.Group
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.feature.groups.R
import ru.bgitu.feature.groups.model.RESULT_PRIMARY_GROUP
import ru.bgitu.feature.groups.model.RESULT_SECONDARY_GROUP
import ru.bgitu.feature.groups.presentation.component.DeleteGroupDialog
import ru.bgitu.feature.groups.presentation.component.PrimaryGroupItem
import ru.bgitu.feature.groups.presentation.component.ReorderableGroupTabs

@Composable
fun GroupsRoute(
    backResultPrimaryGroup: Group?,
    backResultSecondaryGroup: Group?
) {
    val navController = LocalNavController.current
    val viewModel: GroupsViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(backResultPrimaryGroup, backResultSecondaryGroup) {
        backResultPrimaryGroup?.let {
            viewModel.onIntent(GroupsIntent.ChangePrimaryGroup(it))
        }
        backResultSecondaryGroup?.let {
            viewModel.onIntent(GroupsIntent.AddGroup(it))
        }
    }

    GroupsScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onBack = { navController.back() },
        onPrimaryGroupClick = {
            navController.navigate(Screen.GroupSearch(backResultType = RESULT_PRIMARY_GROUP))
        },
        onAddGroupRequest = {
            navController.navigate(Screen.GroupSearch(backResultType = RESULT_SECONDARY_GROUP))
        }
    )
}

@Composable
private fun GroupsScreen(
    uiState: GroupsUiState,
    onIntent: (GroupsIntent) -> Unit,
    onBack: () -> Unit,
    onPrimaryGroupClick: () -> Unit,
    onAddGroupRequest: () -> Unit
) {
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

    ClearFocusWithImeEffect()

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
                    AppBackButton(onClick = onBack,)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(AppTheme.spacing.l)
        ) {

            when (uiState) {
                GroupsUiState.Loading -> {}
                is GroupsUiState.Success -> {
                    PrimaryGroupItem(
                        groupName = uiState.primaryGroup.name,
                        onClick = onPrimaryGroupClick,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(AppTheme.spacing.l))

                    GroupsVisibilitySetting(
                        visible = uiState.showGroupsOnMainScreen,
                        onClick = {
                            onIntent(
                                GroupsIntent.SetGroupVisibility(!uiState.showGroupsOnMainScreen)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(AppTheme.spacing.l))


                    ReorderableGroupTabs(
                        groups = uiState.savedGroups,
                        onChange = {
                            onIntent(GroupsIntent.SetGroups(it))
                        },
                        onAddGroupRequest = onAddGroupRequest,
                        onGroupClick = { groupInDialog = it },
                        enabled = uiState.showGroupsOnMainScreen,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                GroupsUiState.Error -> {}
            }
        }
    }
}

@Composable
private fun GroupsVisibilitySetting(
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
                checked = visible,
                onCheckedChange = { onClick() }
            )
        }
    }
}