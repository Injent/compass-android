package ru.bgitu.app

import android.content.Context
import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.View.LAYER_TYPE_SOFTWARE
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
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bgitu.app.crashscreen.handleUncaughtException
import ru.bgitu.app.presentation.App
import ru.bgitu.app.presentation.MainActivityUiState
import ru.bgitu.app.presentation.MainViewModel
import ru.bgitu.components.updates.api.StoreSync
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.data.migrations.CriticalChangesService
import ru.bgitu.core.designsystem.util.ClearFocusWithImeEffect
import ru.bgitu.feature.schedule_widget.work.WidgetScheduleWorker

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private val storeSync by inject<StoreSync>()

    override fun onCreate(savedInstanceState: Bundle?) {
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
}