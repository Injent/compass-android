package ru.bgitu.feature.login.presentation.login

import android.app.Activity
import android.content.res.Configuration
import android.view.WindowManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.bgitu.core.common.USER_AGREEMENT_URL
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.designsystem.components.AppButton
import ru.bgitu.core.designsystem.components.AppConfirmDialog
import ru.bgitu.core.designsystem.components.AppSecondaryButton
import ru.bgitu.core.designsystem.components.AppSecureTextField
import ru.bgitu.core.designsystem.components.AppSnackbarHost
import ru.bgitu.core.designsystem.components.AppTextField
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.components.rememberSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.util.ClearFocusWithImeEffect
import ru.bgitu.core.designsystem.util.TopGoneLayout
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.Tab
import ru.bgitu.core.navigation.back
import ru.bgitu.core.navigation.navigateTopDestination
import ru.bgitu.core.navigation.push
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.login.R
import ru.bgitu.feature.login.components.UserAgreement

@Composable
fun LoginRoute(
    viewModel: LoginViewModel
) {
    val navController = LocalNavController.current
    val snackbarController = LocalSnackbarController.current

    val context = LocalContext.current

    SideEffect {
        (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }
    viewModel.events.listenEvents { event ->
        when (event) {
            LoginEvent.Back -> {
                snackbarController.dismiss()
                navController.back()
            }
            LoginEvent.ForgotPassword -> {
                navController.push(Screen.Recovery)
            }
            LoginEvent.HideSnackbar -> {
                snackbarController.dismiss()
            }
            is LoginEvent.NavigateToGroupSelection -> {
                navController.push(Screen.PickGroup(predictedSearchQuery = event.searchQuery))
            }
            LoginEvent.NavigateToMainScreen -> {
                navController.navigateTopDestination(Tab.HOME)
                snackbarController.dismiss()
            }
            LoginEvent.ReadUserAgreement -> {
                context.openUrl(USER_AGREEMENT_URL)
            }
            is LoginEvent.ShowErrorSnackbar -> {
                snackbarController.show(
                    message = event.message.asString(context),
                    withDismissAction = true,
                    icon = AppIcons.WarningRed
                )
            }
            else -> Unit
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreen(
        onIntent = viewModel::onIntent,
        uiState = uiState,
        loginFieldState = viewModel.loginFieldState,
        passwordFieldState = viewModel.passwordFieldState
    )
}

@Composable
internal fun LoginScreen(
    uiState: LoginUiState,
    loginFieldState: TextFieldState,
    passwordFieldState: TextFieldState,
    onIntent: (LoginIntent) -> Unit,
) {
    ClearFocusWithImeEffect()

    Scaffold(
        snackbarHost = {
            AppSnackbarHost(modifier = Modifier.imePadding())
        },
        topBar = {
            if (uiState.verificationRequest) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.account_confirmation),
                            style = AppTheme.typography.title3,
                            color = AppTheme.colorScheme.foreground1
                        )
                    },
                    navigationIcon = {
                        AppBackButton(
                            onClick = { onIntent(LoginIntent.Back) },
                        )

                    }
                )
            }
        },
        modifier = Modifier.systemBarsPadding()
    ) { paddingValues ->
        TopGoneLayout(
            top = {
                if (!uiState.verificationRequest) {
                    val isImeVisible = WindowInsets.isImeVisible

                    val alpha by animateFloatAsState(
                        targetValue = if (isImeVisible) 0f else 1f
                    )
                    Image(
                        painter = painterResource(AppIllustrations.SignIn),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(alpha)
                    )
                }
            },
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .padding(
                    horizontal = AppTheme.spacing.l,
                    vertical = AppTheme.spacing.s
                )
        ) {
            LoginSection(
                loginFieldState = loginFieldState,
                passwordFieldState = passwordFieldState,
                uiState = uiState,
                onIntent = onIntent,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun LoginSection(
    loginFieldState: TextFieldState,
    passwordFieldState: TextFieldState,
    uiState: LoginUiState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .focusGroup(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l)
    ) {
        if (!uiState.verificationRequest) {
            Text(
                text = stringResource(R.string.authorization),
                style = AppTheme.typography.largeTitle,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier
            )
        }

        AppTextField(
            state = loginFieldState,
            placeholder = stringResource(R.string.label_login),
            lineLimits = TextFieldLineLimits.SingleLine,
            leadingIcon = {
                Icon(
                    painter = painterResource(AppIcons.Email),
                    contentDescription = null,
                    modifier = Modifier.size(AppTheme.spacing.xl)
                )
            },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            onKeyboardAction = { focusManager.moveFocus(FocusDirection.Next) },
            modifier = Modifier
                .fillMaxWidth()
        )

        AppSecureTextField(
            state = passwordFieldState,
            placeholder = stringResource(R.string.label_password),
            leadingIcon = {
                Icon(
                    painter = painterResource(AppIcons.Lock),
                    contentDescription = null,
                    modifier = Modifier.size(AppTheme.spacing.xl)
                )
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .clickable {
                            onIntent(LoginIntent.SwitchPasswordVisibility)
                        },
                    painter = painterResource(
                        if (uiState.passwordVisible) {
                            AppIcons.VisibilityOn
                        } else {
                            AppIcons.VisibilityOff
                        }
                    ),
                    tint = if (uiState.passwordVisible) {
                        AppTheme.colorScheme.foreground
                    } else {
                        AppTheme.colorScheme.foreground3
                    },
                    contentDescription = stringResource(
                        if (uiState.passwordVisible) {
                            R.string.desc_hide_password
                        } else R.string.desc_show_password
                    )
                )
            },
            textVisible = uiState.passwordVisible,
            imeAction = ImeAction.Go,
            enabled = uiState.canLogin,
            onKeyboardAction = {
                onIntent(LoginIntent.Login)
            },
            modifier = Modifier
                .semantics {
                    contentDescription = context.getString(R.string.desc_login)
                }
                .fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            WhyEosClickableText()
            ForgotPasswordClickableText(
                onClick = {
                    onIntent(LoginIntent.ForgotPassword)
                }
            )
        }

        AppButton(
            text = stringResource(
                if (uiState.verificationRequest) {
                    R.string.confirm_account
                } else R.string.button_login
            ),
            onClick = {
                onIntent(LoginIntent.Login)
            },
            isLoading = uiState.loading,
            enabled = uiState.canLogin,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = context.getString(R.string.desc_login)
                }
        )

        if (!uiState.verificationRequest) {
            Text(
                text = stringResource(R.string.alternative),
                style = AppTheme.typography.headline2.copy(fontWeight = FontWeight.Normal),
                color = AppTheme.colorScheme.foreground4,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            AppSecondaryButton(
                text = stringResource(R.string.sign_in_as_guest),
                onClick = {
                    onIntent(LoginIntent.NavigateToGroupSelection)
                },
                enabled = !uiState.loading,
                modifier = Modifier.fillMaxWidth()
            )

            UserAgreement(
                onUserAgreementClick = {
                    onIntent(LoginIntent.ReadUserAgreement)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ForgotPasswordClickableText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = buildAnnotatedString {
            withLink(
                link = LinkAnnotation.Clickable(
                    tag = "click",
                    linkInteractionListener = { onClick() }
                )
            ) {
                append(stringResource(R.string.forgot_password))
            }
        },
        maxLines = 1,
        style = AppTheme.typography.calloutButton,
        color = AppTheme.colorScheme.foreground,
        modifier = modifier.padding(vertical = AppTheme.spacing.s),
    )
}

@Composable
private fun WhyEosClickableText(
    modifier: Modifier = Modifier
) {
    var dialogOpen by remember { mutableStateOf(false) }

    if (dialogOpen) {
        AppConfirmDialog(
            title = stringResource(R.string.why_eos),
            text = stringResource(R.string.explanation_dialog_text),
            onConfirm = { dialogOpen = false }
        )
    }

    Text(
        text = buildAnnotatedString {
            withLink(
                link = LinkAnnotation.Clickable(
                    tag = "click",
                    linkInteractionListener = { dialogOpen = true }
                ),
            ) {
                append(stringResource(R.string.why_eos))
            }
        },
        style = AppTheme.typography.calloutButton,
        color = AppTheme.colorScheme.foreground,
        maxLines = 1,
        modifier = modifier.padding(vertical = AppTheme.spacing.s),
    )
}

@Preview(locale = "ru")
@Composable
private fun LoginScreenPreview() {
    CompassTheme {
        CompositionLocalProvider(
            LocalSnackbarController provides rememberSnackbarController()
        ) {
            Box(
                Modifier
                    .background(AppTheme.colorScheme.background2)
                    .padding(AppTheme.spacing.l)
            ) {
                LoginScreen(
                    loginFieldState = TextFieldState(),
                    passwordFieldState = TextFieldState(),
                    uiState = LoginUiState(),
                    onIntent = {}
                )
            }
        }
    }
}

@Preview(locale = "ru", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenDarkPreview() {
    LoginScreenPreview()
}