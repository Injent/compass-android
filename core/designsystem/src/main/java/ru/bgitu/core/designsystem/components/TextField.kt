package ru.bgitu.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.theme.applyState

@Composable
fun AppSecureTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String = "",
    textVisible: Boolean = false,
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    imeAction: ImeAction = ImeAction.Default,
    onKeyboardAction: KeyboardActionHandler? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val cursorColor = AppTheme.colorScheme.foreground
    val cursonBrush = remember { SolidColor(cursorColor) }

    val containerColor by animateColorAsState(
        targetValue = AppTheme.colorScheme.backgroundTouchable.applyState(
            isPressed = isFocused,
            enabled = enabled
        ),
        label = "container transition"
    )
    
    val iconColor by animateColorAsState(
        targetValue = if (enabled) {
            AppTheme.colorScheme.foreground3
        } else AppTheme.colorScheme.foreground4,
        label = "container transition"
    )

    Column(modifier) {
        label?.let {
            Text(
                text = label,
                style = AppTheme.typography.callout,
                color = AppTheme.colorScheme.foreground1
            )
            Spacer(Modifier.height(AppTheme.spacing.s))
        }
        BasicSecureTextField(
            state = state,
            modifier = Modifier,
            textStyle = AppTheme.typography.callout.copy(
                color = AppTheme.colorScheme.foreground1
            ),
            enabled = enabled,
            interactionSource = interactionSource,
            textObfuscationMode = if (textVisible) {
                TextObfuscationMode.Visible
            } else TextObfuscationMode.RevealLastTyped,
            onKeyboardAction = onKeyboardAction,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            cursorBrush = cursonBrush,
            decorator = { textLayout ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .background(containerColor, AppTheme.shapes.default)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides iconColor,
                        content = leadingIcon
                    )
                    Box(Modifier.weight(1f)) {
                        textLayout()
                        if (state.text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = AppTheme.typography.callout,
                                color = AppTheme.colorScheme.foreground4
                            )
                        }
                    }
                    CompositionLocalProvider(
                        LocalContentColor provides iconColor,
                        content = trailingIcon
                    )
                }
            },
        )
    }
}

@Composable
fun AppTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    tip: String? = null,
    placeholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    inputTransformation: InputTransformation? = null,
    interactionSource: MutableInteractionSource? = null
) {
    val mutableInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isFocused by mutableInteractionSource.collectIsFocusedAsState()

    val cursorColor = AppTheme.colorScheme.foreground
    val cursonBrush = remember { SolidColor(cursorColor) }

    val containerColor by animateColorAsState(
        targetValue = AppTheme.colorScheme.backgroundTouchable.applyState(
            isPressed = isFocused,
            enabled = enabled
        ),
    )

    val iconColor by animateColorAsState(
        targetValue = if (enabled) {
            AppTheme.colorScheme.foreground3
        } else AppTheme.colorScheme.foreground4,
    )

    Column(modifier) {
        label?.let {
            Text(
                text = label,
                style = AppTheme.typography.subheadline,
                color = AppTheme.colorScheme.foreground2
            )
            Spacer(Modifier.height(AppTheme.spacing.s))
        }
        BasicTextField(
            state = state,
            modifier = Modifier,
            textStyle = AppTheme.typography.callout.copy(
                color = AppTheme.colorScheme.foreground1
            ),
            lineLimits = lineLimits,
            enabled = enabled,
            readOnly = readOnly,
            interactionSource = mutableInteractionSource,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            cursorBrush = cursonBrush,
            inputTransformation = inputTransformation,
            decorator = { textLayout ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 48.dp)
                        .background(containerColor, AppTheme.shapes.default)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides iconColor,
                        content = leadingIcon
                    )
                    Box(Modifier.weight(1f)) {
                        textLayout()
                        if (state.text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = AppTheme.typography.callout,
                                color = AppTheme.colorScheme.foreground4,
                            )
                        }
                    }
                    CompositionLocalProvider(
                        LocalContentColor provides iconColor,
                        content = trailingIcon
                    )
                }
            }
        )

        tip?.let { tip ->
            Text(
                text = tip,
                color = AppTheme.colorScheme.foreground2,
                style = AppTheme.typography.footnote,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
fun AppDecimalField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val cursorColor = AppTheme.colorScheme.foreground
    val cursonBrush = remember { SolidColor(cursorColor) }

    val containerColor by animateColorAsState(
        targetValue = AppTheme.colorScheme.backgroundTouchable.applyState(
            isPressed = isFocused,
            enabled = enabled
        )
    )

    BasicTextField(
        state = state,
        modifier = modifier.focusRequester(focusRequester),
        textStyle = AppTheme.typography.callout.copy(
            color = AppTheme.colorScheme.foreground1
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        enabled = true,
        readOnly = false,
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Decimal
        ),
        cursorBrush = cursonBrush,
        inputTransformation = {
            if (!asCharSequence().all { it.isDigit() } || length > 2) {
                revertAllChanges()
            }
        },
        decorator = { textLayout ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .size(width = 50.dp, height = 36.dp)
                    .background(containerColor, AppTheme.shapes.default)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                if (state.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = AppTheme.typography.callout,
                        color = AppTheme.colorScheme.foreground4
                    )
                } else {
                    textLayout()
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AppTextField2Preview() {
    CompassTheme {
        val state = rememberTextFieldState()

        AppTextField(
            state = state,
            placeholder = "Placeholder",
            trailingIcon = {
                AppIconButton(
                    onClick = {},
                    icon = AppIcons.VisibilityOn,
                    iconPadding = PaddingValues(0.dp)
                )
            },
            label = "Label",
            modifier = Modifier
                .width(200.dp)
                .padding(AppTheme.spacing.s)
        )
    }
}