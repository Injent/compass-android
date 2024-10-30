package ru.bgitu.app

import android.content.Context
import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.webkit.CookieManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bgitu.app.crashscreen.CrashServiceStarter
import ru.bgitu.app.crashscreen.handleUncaughtException
import ru.bgitu.app.presentation.App
import ru.bgitu.app.presentation.MainActivityUiState
import ru.bgitu.app.presentation.MainViewModel
import ru.bgitu.components.sitetraffic.TrafficWebView
import ru.bgitu.components.updates.api.StoreSync
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.data.migrations.CriticalChangesService
import ru.bgitu.core.designsystem.util.ClearFocusWithImeEffect
import ru.bgitu.feature.schedule_widget.work.WidgetScheduleWorker

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private val storeSync by inject<StoreSync>()
    private var trafficWebView: TrafficWebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableCrashlytics()
        handleUncaughtException()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setupWindowRendering()

        var uiState by mutableStateOf(MainActivityUiState())

        initializeWorkers()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                storeSync.sync(this@MainActivity)

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

            LaunchedEffect(uiState.helpSiteTraffic) {
                // Delay time for loading home screen
                delay(3000)
                if (isWebViewSupported() && uiState.helpSiteTraffic && trafficWebView == null) {
                    trafficWebView = TrafficWebView(this@MainActivity)
                    runCatching { trafficWebView?.startTraffic() }
                }
            }
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
    }

    private fun enableCrashlytics() = runCatching {
        get<CrashServiceStarter>().start(this@MainActivity)
    }

    override fun onResume() {
        super.onResume()
        trafficWebView?.onResume()
    }

    override fun onPause() {
        trafficWebView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        trafficWebView?.destroy()
        super.onDestroy()
    }
}

private fun isWebViewSupported() = runCatching { CookieManager.getInstance() }.isSuccess