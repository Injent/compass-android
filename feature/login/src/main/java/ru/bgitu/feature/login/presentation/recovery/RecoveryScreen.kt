package ru.bgitu.feature.login.presentation.recovery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppButton
import ru.bgitu.core.designsystem.components.AppConfirmDialog
import ru.bgitu.core.designsystem.components.AppSnackbarHost
import ru.bgitu.core.designsystem.components.AppTextField
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.rememberImeAnimationState
import ru.bgitu.core.designsystem.util.rememberImeState
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.login.R
import kotlin.time.Duration.Companion.seconds

@Composable
fun RecoveryScreen() {
    val navController = LocalNavController.current
    val snackbarController = LocalSnackbarController.current
    val context = LocalContext.current
    val viewModel: RecoveryViewModel = koinViewModel()

    val webViewState = rememberRecoveryWebViewState()

    LaunchedEffect(webViewState.loadingState) {
        when (val state = webViewState.loadingState) {
            is LoadingState.Error -> {
                viewModel.onIntent(RecoveryIntent.Error(state.details))
            }
            LoadingState.Loading -> {
                viewModel.onIntent(RecoveryIntent.Loading)
            }
            LoadingState.SuccessRecovery -> {
                viewModel.onIntent(RecoveryIntent.Success)
            }
            else -> Unit
        }
    }

    var successDialogOpen by remember {
        mutableStateOf(false)
    }
    viewModel.events.listenEvents { event ->
        when (event) {
            RecoveryEvent.Back -> {
                snackbarController.dismiss()
                navController.back()
            }
            is RecoveryEvent.Submit -> {
                webViewState.submitEmail(event.email)
            }
            RecoveryEvent.HideSnackbar -> {
                snackbarController.dismiss()
            }
            is RecoveryEvent.ShowErrorSnackbar -> {
                snackbarController.show(
                    duration = 5.seconds,
                    message = event.errorText.asString(context),
                    withDismissAction = false,
                    icon = AppIcons.WarningRed
                )
            }
            RecoveryEvent.ShowSuccessDialog -> {
                successDialogOpen = true
            }
        }
    }

    val isEmailValid by viewModel.isEmailValid.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    RecoveryScreenContent(
        isEmailValid = isEmailValid,
        isLoading = isLoading,
        successDialogOpen = successDialogOpen,
        onIntent = viewModel::onIntent,
        emailFieldState = viewModel.emailFieldState
    )
}

@Composable
private fun RecoveryScreenContent(
    isEmailValid: Boolean,
    isLoading: Boolean,
    successDialogOpen: Boolean,
    onIntent: (RecoveryIntent) -> Unit,
    emailFieldState: TextFieldState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1
                    )
                },
                navigationIcon = {
                    AppBackButton(
                        onClick = { onIntent(RecoveryIntent.Back) }
                    )
                }
            )
        },
        snackbarHost = {
            AppSnackbarHost(Modifier.imePadding())
        },
        modifier = Modifier.systemBarsPadding()
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .padding(
                    horizontal = AppTheme.spacing.l,
                    vertical = AppTheme.spacing.s
                )
        ) {
            var fixedImageSize by remember { mutableStateOf(IntSize.Zero) }
            val alpha = rememberImeAnimationState().fraction
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .then(
                        if (fixedImageSize == IntSize.Zero) {
                            Modifier.wrapContentHeight()
                        } else {
                            Modifier
                                .fillMaxWidth()
                                .height(
                                    with(LocalDensity.current) {
                                        (fixedImageSize.height * alpha).toDp()
                                    }
                                )
                                .heightIn(max = 500.dp)
                        }
                    )
                    .onPlaced {
                        if (fixedImageSize == IntSize.Zero)
                            fixedImageSize = it.size
                    }
                    .align(Alignment.CenterHorizontally)
                    .alpha(alpha)
            ) {
                Text(
                    text = stringResource(R.string.enter_your_registration_email),
                    style = AppTheme.typography.body,
                    color = AppTheme.colorScheme.foreground2,
                    textAlign = TextAlign.Center
                )
                Image(
                    painter = painterResource(AppIllustrations.ForgotPassword),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }

            val bringIntoViewRequester = remember { BringIntoViewRequester() }
            val focusManager = LocalFocusManager.current
            val imeOpen by rememberImeState()
            LaunchedEffect(imeOpen) {
                if (imeOpen) {
                    bringIntoViewRequester.bringIntoView()
                } else {
                    focusManager.clearFocus()
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .bringIntoViewRequester(bringIntoViewRequester)
            ) {
                AppTextField(
                    state = emailFieldState,
                    label = stringResource(R.string.label_email),
                    placeholder = stringResource(R.string.placeholder_email),
                    tip = stringResource(R.string.hint_email),
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                AppButton(
                    text = stringResource(R.string.submit_email),
                    onClick = { onIntent(RecoveryIntent.Submit) },
                    isLoading = isLoading,
                    enabled = isEmailValid,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }

    if (successDialogOpen) {
        SuccessRecoveryDialog(
            onConfirm = { onIntent(RecoveryIntent.Back) }
        )
    }
}

@Composable
fun SuccessRecoveryDialog(
    onConfirm: () -> Unit
) {
    AppConfirmDialog(
        title = stringResource(R.string.successful_recovery),
        text = stringResource(R.string.successful_recovery_text),
        onConfirm = onConfirm
    )
}