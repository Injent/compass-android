package ru.bgitu.feature.login.presentation.pickgroup

import android.app.Activity
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.core.common.CYRILLIC_REGEX
import ru.bgitu.core.common.USER_AGREEMENT_URL
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.designsystem.components.AppButton
import ru.bgitu.core.designsystem.components.AppRippleButton
import ru.bgitu.core.designsystem.components.AppSearchField
import ru.bgitu.core.designsystem.components.AppSnackbarHost
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.components.InputRegex
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.ClearFocusWithImeEffect
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.rememberImeState
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Tab
import ru.bgitu.core.navigation.back
import ru.bgitu.core.navigation.navigateTopDestination
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.login.R
import ru.bgitu.feature.login.components.UserAgreement

@Composable
fun PickGroupRoute(
    predictedSearhQuery: String?
) {
    val navController = LocalNavController.current
    val snackbarController = LocalSnackbarController.current
    val context = LocalContext.current

    SideEffect {
        (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }
    val viewModel: PickGroupViewModel = koinViewModel {
        parametersOf(predictedSearhQuery)
    }

    viewModel.events.listenEvents { event ->
        when (event) {
            is PickGroupEvent.Error -> {
                snackbarController.show(
                    message = event.details.asString(context),
                    withDismissAction = true,
                    icon = AppIcons.WarningRed,
                    onAction = snackbarController::dismiss
                )
            }
            PickGroupEvent.SuccessfulAuthentication -> {
                navController.navigateTopDestination(Tab.HOME)
            }
            PickGroupEvent.HideSnackbar -> {
                snackbarController.dismiss()
            }
            PickGroupEvent.Back -> {
                snackbarController.dismiss()
                navController.back()
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PickGroupScreen(
        uiState = uiState,
        searchFieldState = viewModel.searchFieldState,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun PickGroupScreen(
    uiState: PickGroupUiState,
    searchFieldState: TextFieldState,
    onIntent: (PickGroupIntent) -> Unit
) {
    val context = LocalContext.current
    val isImeVisible by rememberImeState()
    ClearFocusWithImeEffect()

    BackHandler {
        onIntent(PickGroupIntent.ResetGroup)
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            PickGroupTopBar(onBack = { onIntent(PickGroupIntent.Back) })
        },
        snackbarHost = {
            AppSnackbarHost(
                modifier = Modifier.imePadding()
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = uiState.selectedGroup != null
                        || uiState.searchResults.isEmpty()
                        && !isImeVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .padding(
                        horizontal = AppTheme.spacing.l,
                        vertical = AppTheme.spacing.s
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l)
                ) {
                    AppButton(
                        text = stringResource(R.string.button_continue),
                        onClick = { onIntent(PickGroupIntent.Continue) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.selectedGroup != null
                    )
                    UserAgreement(
                        onUserAgreementClick = {
                            context.openUrl(USER_AGREEMENT_URL)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = AppTheme.spacing.l)
        ) {
            uiState.selectedGroup?.let { group ->
                GroupConfirmationBlock(
                    groupName = group.name,
                    onCancel = { onIntent(PickGroupIntent.ResetGroup) },
                    modifier = Modifier.fillMaxWidth()
                )
            } ?: run {
                GroupSearchField(
                    searchFieldState = searchFieldState,
                    uiState = uiState,
                    onIntent = onIntent,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PickGroupTopBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.pick_group),
                style = AppTheme.typography.title3,
                color = AppTheme.colorScheme.foreground1
            )
        },
        navigationIcon = {
            AppBackButton(onClick = onBack)
        }
    )
}

@Composable
private fun GroupSearchField(
    searchFieldState: TextFieldState,
    uiState: PickGroupUiState,
    onIntent: (PickGroupIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        AppSearchField(
            state = searchFieldState,
            inputTransformation = remember { InputRegex(CYRILLIC_REGEX) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )

        if (uiState.searchResults.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.l),
                contentPadding = WindowInsets.ime.asPaddingValues()
            ) {
                items(
                    items = uiState.searchResults,
                    key = { it.id }
                ) { item ->
                    SearchItem(
                        label = item.name,
                        onClick = {
                            onIntent(PickGroupIntent.PickGroup(item))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .thenIf(uiState.searchResults.first() == item) {
                                clip(AppTheme.shapes.defaultTopCarved)
                            }
                            .then(
                                if (uiState.searchResults.last() != item) {
                                    Modifier.padding(bottom = 1.5.dp)
                                } else Modifier.clip(AppTheme.shapes.defaultBottomCarved)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppRippleButton(
        text = label,
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = AppTheme.spacing.xxxl),
        modifier = modifier
    )
}

@Composable
private fun GroupConfirmationBlock(
    groupName: String,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(AppIllustrations.GroupOfStudent),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Text(
            text = stringResource(R.string.your_selected_group, groupName),
            style = AppTheme.typography.headline2,
            color = AppTheme.colorScheme.foreground1
        )
        Spacer(Modifier.height(AppTheme.spacing.l))
        AppTextButton(
            onClick = onCancel,
            text = stringResource(android.R.string.cancel),
            color = AppTheme.colorScheme.foregroundError
        )
        Spacer(Modifier.height(AppTheme.spacing.l))
    }
}