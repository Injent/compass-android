package ru.bgitu.components.signin.telegram

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.bgitu.components.signin.AuthClient
import ru.bgitu.components.signin.model.AuthMethod
import ru.bgitu.components.signin.model.SignInParams
import ru.bgitu.core.common.Result
import java.util.UUID

private const val TELEGRAM_DEEPLINK = "tg://resolve?domain=sbdfiusdfbsfhbsjhbdhfhnbot&start"

class TelegramAuthClient(
    private val activity: ComponentActivity,
    private val coroutineScope: CoroutineScope,
    private val onResult: (Result<SignInParams>) -> Unit,
) : AuthClient {

    override fun signIn() {
        val idToken = UUID.randomUUID().toString()
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = "$TELEGRAM_DEEPLINK=$idToken".toUri()
        }

        activity.startActivity(intent)

        coroutineScope.launch {
            activity.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                onResult(Result.Success(createSignInParams(idToken)))
            }
        }
    }

    private fun createSignInParams(idToken: String) = SignInParams(
        authMethod = AuthMethod.TELEGRAM,
        idToken = idToken,
        fullName = "",
        avatarUrl = null
    )
}