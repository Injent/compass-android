package ru.bgitu.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun AppChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape
) {
    val color by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colorScheme.foreground
        } else AppTheme.colorScheme.backgroundTouchable,
        label = "transition"
    )
    Surface(
        modifier = modifier.height(36.dp),
        color = color,
        shape = shape,
        contentColor = if (selected) {
            AppTheme.colorScheme.foregroundOnBrand
        } else AppTheme.colorScheme.foreground,
        onClick = onClick
    ) {
        Text(
            text = label,
            style = AppTheme.typography.calloutButton,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(
                    vertical = 8.dp,
                    horizontal = 12.dp
                )
        )
    }
}