package ru.bgitu.feature.login.presentation.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import ru.bgitu.components.signin.AuthClient
import ru.bgitu.components.signin.google.GoogleAuthClient
import ru.bgitu.components.signin.isGooglePlayServicesAvailable
import ru.bgitu.components.signin.model.SignInParams
import ru.bgitu.components.signin.telegram.TelegramAuthClient
import ru.bgitu.core.common.Result
import ru.bgitu.core.common.TextResource
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun OneTaps(
    onResult: (Result<SignInParams>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var authClient by remember {
        mutableStateOf(null as AuthClient?)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l)
    ) {
        com.vk.id.onetap.compose.onetap.OneTap(
            style = if (AppTheme.isDarkTheme) {
                OneTapStyle.Dark(
                    cornersStyle = OneTapButtonCornersStyle.Custom(12f),
                    sizeStyle = OneTapButtonSizeStyle.LARGE_48
                )
            } else {
                OneTapStyle.Light(
                    cornersStyle = OneTapButtonCornersStyle.Custom(12f),
                    sizeStyle = OneTapButtonSizeStyle.LARGE_48
                )
            },
            onAuth = { _, accessToken ->
                authClient = AuthClient.createClient(
                    activity = context as ComponentActivity,
                    coroutineScope = coroutineScope,
                    accessToken = accessToken,
                    onResult = onResult
                )
                authClient?.signIn()
            },
            onFail = { _, fail ->
                onResult(Result.Failure(details = TextResource.Plain(fail.description)))
            },
            modifier = Modifier
                .height(48.dp)
        )
        TelegramSignInButton(
            onClick = {
                authClient = AuthClient.createClient<TelegramAuthClient>(
                    activity = context as ComponentActivity,
                    coroutineScope = coroutineScope,
                    onResult = onResult
                )
                authClient?.signIn()
            },
            modifier = Modifier.fillMaxWidth()
        )
        val googleAuthAvailable = remember {
            context.isGooglePlayServicesAvailable()
        }

        if (googleAuthAvailable) {
            GoogleSignInButton(
                onClick = {
                    authClient = AuthClient.createClient<GoogleAuthClient>(
                        activity = context as ComponentActivity,
                        coroutineScope = coroutineScope,
                        onResult = onResult
                    )
                    authClient?.signIn()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}