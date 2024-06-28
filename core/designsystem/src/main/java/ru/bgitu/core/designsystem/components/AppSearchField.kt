package ru.bgitu.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.icon.AppIcons

class InputRegex(private val pattern: String) : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        val matches = this.asCharSequence().matches(Regex(pattern))
        if (!matches) {
            this.revertAllChanges()
        }
    }
}

@Composable
fun AppSearchField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = stringResource(android.R.string.search_go),
    inputTransformation: InputTransformation? = null,
    onKeyboardAction: KeyboardActionHandler? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    AppTextField(
        state = state,
        modifier = modifier.height(40.dp),
        lineLimits = TextFieldLineLimits.SingleLine,
        label = label,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        leadingIcon = {
            Icon(
                painter = painterResource(AppIcons.Search),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = state.text.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    painter = painterResource(AppIcons.CloseSmall),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { state.clearText() }
                )
            }
        },
        onKeyboardAction = onKeyboardAction,
        inputTransformation = inputTransformation,
        interactionSource = interactionSource
    )
}