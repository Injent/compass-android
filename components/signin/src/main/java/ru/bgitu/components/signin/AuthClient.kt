package ru.bgitu.components.signin

import android.content.Context
import androidx.activity.ComponentActivity
import com.google.android.gms.common.GoogleApiAvailability
import com.vk.id.AccessToken
import kotlinx.coroutines.CoroutineScope
import ru.bgitu.components.signin.google.GoogleAuthClient
import ru.bgitu.components.signin.model.SignInParams
import ru.bgitu.components.signin.telegram.TelegramAuthClient
import ru.bgitu.components.signin.vk.VkAuthClient
import ru.bgitu.core.common.Result


interface AuthClient {
    fun signIn()

    companion object {

        inline fun <reified T : AuthClient> createClient(
            activity: ComponentActivity,
            coroutineScope: CoroutineScope,
            accessToken: AccessToken? = null,
            noinline onResult: (Result<SignInParams>) -> Unit
        ): T {
            return when (T::class) {
                GoogleAuthClient::class -> GoogleAuthClient(
                    context = activity,
                    coroutineScope = coroutineScope,
                    onResult = onResult
                ) as T
                VkAuthClient::class -> VkAuthClient(
                    accessToken = checkNotNull(accessToken),
                    coroutineScope = coroutineScope,
                    onResult = onResult
                ) as T
                TelegramAuthClient::class -> TelegramAuthClient(
                    activity = activity,
                    coroutineScope = coroutineScope,
                    onResult = onResult
                ) as T
                else -> error("Auth client not resolved")
            }
        }
    }
}

fun Context.isGooglePlayServicesAvailable(): Boolean {
    return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) ==
            com.google.android.gms.common.ConnectionResult.SUCCESS
}