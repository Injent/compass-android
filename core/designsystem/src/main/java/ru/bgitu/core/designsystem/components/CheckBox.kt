package ru.bgitu.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.R
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme

@Composable
fun AppCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    shape: Shape = RoundedCornerShape(6.dp),
    enabled: Boolean = true
) {
    val (checkedColor, checkColor, borderColor) = when {
        checked && enabled -> arrayOf(AppTheme.colorScheme.foreground, AppTheme.colorScheme.foregroundOnBrand, null)
        !checked && enabled -> arrayOf(AppTheme.colorScheme.background1, null, AppTheme.colorScheme.foreground3)
        checked && !enabled -> arrayOf(AppTheme.colorScheme.foreground3, AppTheme.colorScheme.backgroundTouchable, null)
        else -> arrayOf(AppTheme.colorScheme.foreground, null, AppTheme.colorScheme.foreground3)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(checkedColor!!, shape)
                .then(
                    borderColor?.let {
                        Modifier.border(1.dp, it, shape)
                    } ?: Modifier
                )
                .clickable(
                    enabled = enabled,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onCheckedChange(!checked) }
                )
                .padding(1.dp)
        ) {
            checkColor?.let {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = it,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(20.dp)
                )
            }
        }
        text?.let {
            Text(
                text = it,
                style = AppTheme.typography.callout,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier.padding(top = 1.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppCheckBoxPreview() {
    CompassTheme {
        Row(Modifier.padding(AppTheme.spacing.s)) {
            AppCheckBox(
                checked = false,
                onCheckedChange = {},
                text = "Checkbox text"
            )
        }
    }
}