package ru.bgitu.app

import android.content.Context
import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.GlobalContext
import ru.bgitu.app.crashscreen.handleUncaughtException
import ru.bgitu.app.di.AppModule
import ru.bgitu.app.presentation.App
import ru.bgitu.app.presentation.MainActivityUiState
import ru.bgitu.app.presentation.MainViewModel
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.StoreSync
import ru.bgitu.core.data.repository.NotificationRepository
import ru.bgitu.core.data.util.SyncManager
import ru.bgitu.core.designsystem.util.ClearFocusWithImeEffect
import ru.bgitu.core.notifications.channels.AppChannelManager
import ru.bgitu.feature.schedule_notifier.api.ScheduleNotifier

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private val storeSync by inject<StoreSync>()

    override fun onCreate(savedInstanceState: Bundle?) {
        handleUncaughtException()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        GlobalContext.loadKoinModules(AppModule)
        initComponents()
        setupWindowRendering()

        var uiState by mutableStateOf(MainActivityUiState())

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                storeSync.sync(this@MainActivity)

                viewModel.uiState.collectLatest {
                    uiState = it
                }
            }
        }

        splashScreen.setKeepOnScreenCondition { uiState.isLoading }

        setContent {
            ClearFocusWithImeEffect()

            App(
                uiState = uiState,
                viewModel = viewModel,
                dynamicColorAvailable = SDK_INT >= 32 && uiState.useDynamicTheme,
            )
        }
    }

    private fun initComponents() {
        get<AppChannelManager>().createChannels()
        get<AppUpdateManager>().safelyClearUpdates()
        get<SyncManager>().requestSync()
        get<ScheduleNotifier>().restart()
        lifecycleScope.launch {
            get<NotificationRepository>().init()
        }
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
        if (SDK_INT in 29..34) {
            @Suppress("DEPRECATION")
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }
    }

//    private fun startTraffic() {
//        val isWebViewSupported = runCatching { CookieManager.getInstance() }.isSuccess
//        if (trafficWebView == null && isWebViewSupported) {
//            trafficWebView = TrafficWebView(this@MainActivity)
//            runCatching { trafficWebView?.startTraffic() }
//            println("Traffic WebView Started")
//        }
//    }
}