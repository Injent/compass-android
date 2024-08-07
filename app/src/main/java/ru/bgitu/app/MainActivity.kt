package ru.bgitu.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.http.X509TrustManagerExtensions
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.View.LAYER_TYPE_SOFTWARE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.ktor.network.tls.TLSConfigBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bgitu.app.crashscreen.handleUncaughtException
import ru.bgitu.app.presentation.App
import ru.bgitu.app.presentation.MainActivityUiState
import ru.bgitu.app.presentation.MainViewModel
import ru.bgitu.components.sync.workers.SyncWorker
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.common.eventbus.EventBus
import ru.bgitu.core.common.eventbus.GlobalAppEvent
import ru.bgitu.core.data.migrations.CriticalChangesService
import ru.bgitu.core.designsystem.util.ClearFocusWithImeEffect
import ru.bgitu.feature.schedule_widget.work.WidgetScheduleWorker
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        handleUncaughtException()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setupWindowRendering()

        var uiState by mutableStateOf(MainActivityUiState())

        initializeWorkers()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState = it }
            }
        }

        splashScreen.setKeepOnScreenCondition { uiState.isLoading }

        setContent {
            ClearFocusWithImeEffect()

            App(
                uiState = uiState,
                viewModel = viewModel,
                dynamicColorAvailable = SDK_INT >= 31,
            )
        }
    }

    private fun initializeWorkers() {
        WidgetScheduleWorker.start(this, DateTimeUtil.currentDate)
        CriticalChangesService.fix(this, BuildConfig.CRITIVAL_VERSIONS_CODE)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        overrideLargeFontSize()
    }

    // Preventing large fonts to destroy the UI
    private fun overrideLargeFontSize() {
        Configuration().apply {
            setTo(baseContext.resources.configuration)
            fontScale = fontScale.coerceAtMost(1.5f)
            applyOverrideConfiguration(this)
        }
    }

    private fun setupWindowRendering() {
        enableEdgeToEdge()
        // Transparent system bars
        if (SDK_INT >= 29) {
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }
        // Required for shadows and animation rendering on API < 29
        if (SDK_INT < 29) {
            window.decorView.rootView.setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
    }

    private fun requestNotificationPermission() {
        if (SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            )
        {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }
}

@SuppressLint("CustomX509TrustManager")
class MyTrustManager(private val config: TLSConfigBuilder) : X509TrustManager {
    private val delegate = config.build().trustManager
    private val extensions = X509TrustManagerExtensions(delegate)

    override fun checkClientTrusted(certificates: Array<out X509Certificate>?, authType: String?) {}

    override fun checkServerTrusted(certificates: Array<out X509Certificate>?, authType: String?) {}

    override fun getAcceptedIssuers(): Array<X509Certificate> = delegate.acceptedIssuers
}