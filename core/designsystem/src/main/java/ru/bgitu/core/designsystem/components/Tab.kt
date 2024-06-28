package ru.bgitu.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun AppTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    shape: Shape = AppTheme.shapes.default,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = AppTheme.spacing.m,
        vertical = AppTheme.spacing.s
    )
) {
    val color by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colorScheme.foreground1
        } else AppTheme.colorScheme.foregroundDisabled,
        label = "transition"
    )

    Surface(
        color = backgroundColor,
        shape = shape,
        onClick = onClick,
        modifier = modifier
            .height(AppTabTokens.Height)
    ) {
        Box(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            Text(
                text = text,
                style = AppTheme.typography.callout,
                color = color,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

object AppTabTokens {
    val Height = 40.dp
}