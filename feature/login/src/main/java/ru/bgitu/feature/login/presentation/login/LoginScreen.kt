package ru.bgitu.feature.login.presentation.login

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.USER_AGREEMENT_URL
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.designsystem.components.AppSecondaryButton
import ru.bgitu.core.designsystem.components.AppSnackbarHost
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.components.rememberSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.illustration.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.util.TopGoneLayout
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.rememberImeState
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.login.R
import ru.bgitu.feature.login.presentation.components.OneTaps
import ru.bgitu.feature.login.presentation.components.UserAgreement

@Composable
fun LoginRoute(
    viewModel: LoginViewModel
) {
    val navController = LocalNavController.current
    val snackbarController = LocalSnackbarController.current

    val context = LocalContext.current
    
    viewModel.events.listenEvents { event ->
        when (event) {
            LoginEvent.Back -> {
                snackbarController.dismiss()
                navController.back()
            }
            LoginEvent.HideSnackbar -> {
                snackbarController.dismiss()
            }
            LoginEvent.NavigateToMainScreen -> {
                navController.navigate(Screen.Home) {
                    popUpTo<Screen.Login> {
                        inclusive = true
                    }

                    launchSingleTop = true
                    restoreState = true
                }
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
    )
}

@Composable
internal fun LoginScreen(
    uiState: LoginUiState,
    onIntent: (LoginIntent) -> Unit,
) {
    val isImeVisible by rememberImeState()

    Scaffold(
        snackbarHost = {
            AppSnackbarHost(modifier = Modifier.imePadding())
        },
        modifier = Modifier.systemBarsPadding()
    ) { paddingValues ->
        TopGoneLayout(
            top = {
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
    uiState: LoginUiState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .focusGroup(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l)
    ) {
        Text(
            text = stringResource(R.string.authorization),
            style = AppTheme.typography.largeTitle,
            color = AppTheme.colorScheme.foreground1,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(AppTheme.spacing.xxxl)
        )

        OneTaps(
            onResult = { signInParams ->
                if (signInParams is Result.Success) {
                    onIntent(LoginIntent.SignIn(signInParams.value))
                } else {
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = context.getString(R.string.desc_login)
                },
        )

        Text(
            text = stringResource(R.string.alternative),
            style = AppTheme.typography.headline2.copy(fontWeight = FontWeight.Normal),
            color = AppTheme.colorScheme.foreground4,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        AppSecondaryButton(
            text = stringResource(R.string.sign_in_as_guest),
            onClick = {
                onIntent(LoginIntent.SignIn(null))
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