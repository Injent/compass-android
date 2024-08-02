package ru.bgitu.components.signin.google

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.bgitu.components.signin.AuthClient
import ru.bgitu.components.signin.BuildConfig
import ru.bgitu.components.signin.model.AuthMethod
import ru.bgitu.components.signin.model.SignInParams
import ru.bgitu.core.common.Result

class GoogleAuthClient(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val onResult: (Result<SignInParams>) -> Unit
) : AuthClient {

    override fun signIn() {
        coroutineScope.launch {
            runCatching {
                val credentialManager = CredentialManager.create(context)

                val credential = credentialManager.getCredential(
                    request = buildCredentialRequest(),
                    context = context
                ).credential

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                googleIdTokenCredential.idToken
            }
                .onSuccess { googleIdToken ->
                    val firebaseUser = Firebase.auth.signInWithCredential(
                        GoogleAuthProvider.getCredential(googleIdToken, null)
                    ).await().user

                    if (firebaseUser == null) {
                        onResult(Result.Failure(NullPointerException()))
                        return@launch
                    }

                    onResult(
                        Result.Success(
                            createSignInParams(
                                firebaseUser = firebaseUser,
                                idToken = googleIdToken
                            )
                        )
                    )
                }
                .onFailure { e ->
                    onResult(Result.Failure(throwable = e))
                }
        }
    }

    private fun buildCredentialRequest(): GetCredentialRequest {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.FIREBASE_WEBCLIENT_ID)
            .setAutoSelectEnabled(true)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    private fun createSignInParams(firebaseUser: FirebaseUser, idToken: String) = SignInParams(
        authMethod = AuthMethod.GOOGLE,
        idToken = idToken,
        fullName = firebaseUser.displayName.orEmpty(),
        avatarUrl = firebaseUser.photoUrl?.toString().orEmpty()
    )
}