package ru.bgitu.feature.settings.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppSwitch
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun SettingsOption(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(modifier) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground
        )
        Spacer(Modifier.width(AppTheme.spacing.l))
        content()
    }
}

@Composable
internal fun SwitchOption(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String = "",
) {
    Row(
        modifier = modifier
            .pointerInput(checked) {
                detectTapGestures {
                    onCheckedChange(!checked)
                }
            }
    ) {
        OptionsDescription(
            name = text,
            description = description,
            modifier = Modifier.weight(1f)
        )
        AppSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(top = AppTheme.spacing.s)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ChipsOption(
    text: String,
    itemCount: Int,
    modifier: Modifier = Modifier,
    item: @Composable (index: Int) -> Unit
) {
    Column(modifier) {
        Text(
            text = text,
            style = AppTheme.typography.headline1,
            color = AppTheme.colorScheme.foreground1
        )
        Spacer(Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s)
        ) {
            (0..<itemCount).forEach {
                item(it)
            }
        }
    }
}

@Composable
internal fun OptionsDescription(
    name: String,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = name,
            style = AppTheme.typography.headline1,
            color = AppTheme.colorScheme.foreground1
        )
        description?.let {
            if (it.isNotEmpty()) {
                Spacer(Modifier.height(AppTheme.spacing.s))
                Text(
                    text = it,
                    style = AppTheme.typography.callout,
                    color = AppTheme.colorScheme.foreground2
                )
            }
        }
    }
}