package ru.bgitu.feature.input

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import ru.bgitu.core.designsystem.components.AppConfirmDialog
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.components.AppTextField
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.feature.input.navigation.Input

@Composable
fun InputScreenRoute(params: Input) {
    val navController = LocalNavController.current

    InputScreen(
        params = params,
        onBack = navController::back,
        onDone = { text ->
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(params.resultKey, text)
            navController.back()
        }
    )
}

@Composable
private fun InputScreen(
    params: Input,
    onBack: () -> Unit,
    onDone: (String) -> Unit
) {
    val inputState = rememberSaveable(
        saver = Saver(
            save = { state: TextFieldState ->
                state.text.toString()
            },
            restore = { s: String ->
                TextFieldState(initialText = s)
            }
        )
    ) { TextFieldState(initialText = params.initialText) }

    var exitDialogOpen by remember { mutableStateOf(false) }

    BackHandler {
        exitDialogOpen = true
    }

    if (exitDialogOpen) {
        ExitDialog(
            onConfirm = {
                exitDialogOpen = false
                onDone(inputState.text.toString())
            },
            onDismissRequest = {
                exitDialogOpen = false
                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = params.title,
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    AppBackButton(
                        onClick = {
                            exitDialogOpen = false
                            onBack()
                        }
                    )
                },
                actions = {
                    AppTextButton(
                        text = stringResource(R.string.action_save),
                        onClick = {
                            exitDialogOpen = false
                            onDone(inputState.text.toString())
                        }
                    )
                }
            )
        },
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(AppTheme.spacing.l)
        ) {
            Text(
                text = params.description,
                style = AppTheme.typography.subheadline,
                color = AppTheme.colorScheme.foreground3
            )

            val focusRequester = remember { FocusRequester() }
            var fieldFocusCount by rememberSaveable { mutableIntStateOf(0) }

            AppTextField(
                state = inputState,
                placeholder = params.placeholder,
                inputTransformation = remember { Input.validator },
                lineLimits = if (params.maxLines > 1) {
                    TextFieldLineLimits.MultiLine(minHeightInLines = params.maxLines)
                } else TextFieldLineLimits.SingleLine,
                onKeyboardAction = {
                    onDone(inputState.text.toString())
                },
                maxLines = params.maxLines,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { state ->
                        if (!state.isFocused && fieldFocusCount > 0) {
                            exitDialogOpen = true
                        } else {
                            fieldFocusCount++
                        }
                    },
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}

@Composable
private fun ExitDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppConfirmDialog(
        modifier = modifier,
        title = stringResource(R.string.title_save_changes),
        onConfirm = onConfirm,
        onDismiss = onDismissRequest,
        confirmText = stringResource(R.string.action_yes),
        cancelText = stringResource(R.string.action_no),
        bottomAligned = true
    )
}