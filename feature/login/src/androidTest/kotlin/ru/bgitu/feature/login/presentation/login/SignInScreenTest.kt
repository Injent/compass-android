package ru.bgitu.feature.login.presentation.login

import androidx.activity.ComponentActivity
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.Rule
import org.junit.Test
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.components.SnackbarController
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.feature.login.R

class SignInScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun passwordField_isHidingPassword() {
        composeTestRule.setContent {
            ProvideLocalComposition {
                CompassTheme {
                    LoginScreen(
                        uiState = LoginUiState(),
                        loginFieldState = TextFieldState(),
                        passwordFieldState = TextFieldState(),
                        onIntent = {}
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.desc_show_password)
            )
            .assertExists()
    }

    @Test
    fun loginButton_isDisabled_whenCredentialsIsEmpty() {
        composeTestRule.setContent {
            ProvideLocalComposition {
                CompassTheme {
                    LoginScreen(
                        uiState = LoginUiState(),
                        loginFieldState = TextFieldState(),
                        passwordFieldState = TextFieldState(),
                        onIntent = {}
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.desc_login)
            )
            .assertExists()
            .assertIsNotEnabled()
    }

    @Test
    fun loginButton_isEnabled_whenCredentialsIsEntered() {
        composeTestRule.setContent {
            ProvideLocalComposition {
                CompassTheme {
                    LoginScreen(
                        uiState = LoginUiState(
                            canLogin = true
                        ),
                        loginFieldState = TextFieldState("login"),
                        passwordFieldState = TextFieldState("password"),
                        onIntent = {}
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.desc_login)
            )
            .assertExists()
            .assertIsEnabled()
    }

    @Composable
    private fun ProvideLocalComposition(content: @Composable () -> Unit) {
        val coroutineScope = rememberCoroutineScope()
        val snackbarController = remember { SnackbarController(coroutineScope) }

        CompositionLocalProvider(
            LocalSnackbarController provides snackbarController,
            content = content
        )
    }
}