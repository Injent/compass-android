package ru.bgitu.app.presentation

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bgitu.app.BuildConfig
import ru.bgitu.app.crash_screen.handleUncaughtException
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.data.migrations.CriticalChangesService
import ru.bgitu.feature.schedule_widget.widget.EXTRA_OPENED_SCHEDULE_DATE
import ru.bgitu.feature.schedule_widget.work.ScheduleWorker

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep Screen loading when account data is loading
        var uiState by mutableStateOf(MainActivityUiState())

        initializeComponents()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition { uiState.isLoading }

        setContent {
            App(
                uiState = uiState,
                viewModel = viewModel,
                dynamicColorAllowed = SDK_INT >= 31,
            )
        }
    }

    private fun initializeComponents() {
        enableEdgeToEdge()
        if (SDK_INT >= Build.VERSION_CODES.Q) {
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }
        requestNotificationPermission()
        handleUncaughtException()
        ScheduleWorker.start(this, DateTimeUtil.currentDate)
        CriticalChangesService.fix(this, BuildConfig.CRITIVAL_VERSIONS_CODE)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        overrideLargeFontSize()
    }

    private fun overrideLargeFontSize() {
        Configuration().apply {
            setTo(baseContext.resources.configuration)
            fontScale = fontScale.coerceAtMost(1.5f)
            applyOverrideConfiguration(this)
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