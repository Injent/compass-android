package ru.bgitu.feature.login.presentation.recovery

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.bgitu.core.common.TextResource
import ru.bgitu.feature.login.BuildConfig
import ru.bgitu.feature.login.R
import kotlin.time.Duration.Companion.seconds

private const val REG_RESULT_SUCCESS = "RegSuccess=1"
private const val RECOVERY_PASSWORD_ENDPOINT = "${BuildConfig.EOS_URL}/Account/Register.aspx?fPass=1"

sealed class LoadingState {
    data object Unknown : LoadingState()
    data object Loading : LoadingState()
    data object SuccessRecovery : LoadingState()
    data class Error(val details: TextResource) : LoadingState()
}

@Stable
class RecoveryWebViewState(context: Context) {
    var loadingState: LoadingState by mutableStateOf(LoadingState.Unknown)
        private set

    private val webView by mutableStateOf(
        RecoveryWebView(
            context = context,
            onStateChange = {
                if (loadingState != LoadingState.SuccessRecovery)
                    loadingState = it
            }
        )
    )

    fun submitEmail(email: String) {
        webView.submitEmail(email)
    }

    fun onDispose() {
        webView.close()
    }
}

@Composable
fun rememberRecoveryWebViewState(): RecoveryWebViewState {
    val context = LocalContext.current
    val state = remember {
        RecoveryWebViewState(context)
    }
    DisposableEffect(Unit) {
        onDispose(state::onDispose)
    }
    return state
}

private const val TIMEOUT_SECONDS = 10

@SuppressLint("ViewConstructor")
class RecoveryWebView(
    context: Context,
    private val onStateChange: (LoadingState) -> Unit
) : WebView(context) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var timeoutTimerJob: Job? = null
    private var emailWasReceived = false

    private val recoveryClient = object : WebViewClient() {
        @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            url?.let(::handleUrlRedirect)
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            request?.url?.toString()?.let(::handleUrlRedirect)
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            if (emailWasReceived && REG_RESULT_SUCCESS !in url.orEmpty()) {
                onStateChange(LoadingState.Error(TextResource.Id(R.string.email_not_found)))
            }
            super.onPageFinished(view, url)
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            if (request?.url?.toString()?.contains(RECOVERY_PASSWORD_ENDPOINT) == true) {
                onStateChange(LoadingState.Error(TextResource.Id(R.string.failed_to_connect_to_eos)))
            }
        }
    }

    init {
        webViewClient = recoveryClient
        settings.apply {
            @SuppressLint("SetJavaScriptEnabled")
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        loadUrl(RECOVERY_PASSWORD_ENDPOINT)
    }

    fun submitEmail(email: String) {
        onStateChange(LoadingState.Loading)
        emailWasReceived = true
        timeoutTimerJob = coroutineScope.launch {
            delay(TIMEOUT_SECONDS.seconds)
            onStateChange(LoadingState.Error(TextResource.Id(R.string.failed_to_connect_to_eos)))
            timeoutTimerJob?.cancelAndJoin()
            timeoutTimerJob = null
        }
        evaluateJavascript(javaScriptInputCode(email), null)
    }

    fun close() {
        clearCache(true)
        clearHistory()
        clearMatches()
        clearSslPreferences()
        destroy()
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
        WebStorage.getInstance().deleteAllData()
    }

    private fun handleUrlRedirect(url: String) {
        if (REG_RESULT_SUCCESS in url) {
            onStateChange(LoadingState.SuccessRecovery)
        }
    }

    private fun javaScriptInputCode(email: String): String {
        return """
            (function() {
                var el = document.getElementById("ctl00_MainContent_ucRegForm_tbEmail_I");
                el.value = "$email";
                var evt = document.createEvent("Events");
                evt.initEvent("change", true, true);
                el.dispatchEvent(evt);
                var btn = document.getElementById("ctl00_MainContent_ucRegForm_btnCreateUser");
                btn.click();
            })();
        """.trimIndent()
    }
}

