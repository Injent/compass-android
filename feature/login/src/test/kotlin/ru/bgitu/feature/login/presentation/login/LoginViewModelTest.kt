package ru.bgitu.feature.login.presentation.login

import androidx.test.core.app.ActivityScenario.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.bgitu.core.data_test.AlwaysOnlineMonitor
import ru.bgitu.core.testing.repository.TestCompassAuthenticator
import ru.bgitu.core.testing.repository.TestSyncManager
import ru.bgitu.core.testing.util.MainDispatcherRule
import kotlin.random.Random
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginViewModelTest {

    @get:Rule
    val dispatchRule = MainDispatcherRule()

    val testDispatcher = TestScope(UnconfinedTestDispatcher())

    // Keep this during all tests
    private val compassAuthenticator = TestCompassAuthenticator()
    private val syncManager = TestSyncManager()
    private val networkMonitor = AlwaysOnlineMonitor()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel(
            compassAuthenticator = compassAuthenticator,
            syncManager = syncManager,
            networkMonitor = networkMonitor,
            verificationRequest = false
        )
    }

    @Test
    fun `ui state is not loading when initialized`() {
        assertFalse(viewModel.uiState.value.loading)
    }

    @Test
    fun `password is not visible when initialized`() {
        assertFalse(viewModel.uiState.value.passwordVisible)
    }

    @Test
    fun `password toggle is working`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.onIntent(LoginIntent.SwitchPasswordVisibility)

        assertTrue(viewModel.uiState.value.passwordVisible)

        viewModel.onIntent(LoginIntent.SwitchPasswordVisibility)

        assertFalse(viewModel.uiState.value.passwordVisible)

        collectJob.cancel()
    }
}