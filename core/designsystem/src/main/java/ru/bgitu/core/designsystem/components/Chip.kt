package ru.bgitu.core.designsystem.components

import android.view.SoundEffectConstants
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
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
    val view = LocalView.current

    val color by animateColorAsState(
        targetValue = if (selected) {
            AppTheme.colorScheme.backgroundBrand
        } else AppTheme.colorScheme.background2,
        label = "transition"
    )
    Surface(
        modifier = modifier
            .height(36.dp),
        color = color,
        shape = shape,
        contentColor = if (selected) {
            AppTheme.colorScheme.foregroundOnBrand
        } else AppTheme.colorScheme.foreground,
        onClick = {
            onClick()
            view.playSoundEffect(SoundEffectConstants.CLICK)
        }
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

@Composable
fun AppChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    selectedColor: Color,
    color: Color,
    shape: Shape = CircleShape
) {
    val view = LocalView.current

    val animatedColor by animateColorAsState(
        targetValue = if (selected) {
            selectedColor
        } else color,
        label = "transition"
    )
    Surface(
        modifier = modifier
            .height(36.dp),
        color = animatedColor,
        shape = shape,
        contentColor = if (selected) {
            AppTheme.colorScheme.foregroundOnBrand
        } else AppTheme.colorScheme.foreground,
        onClick = {
            onClick()
            view.playSoundEffect(SoundEffectConstants.CLICK)
        }
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