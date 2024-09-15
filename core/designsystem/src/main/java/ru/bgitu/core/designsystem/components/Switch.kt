package ru.bgitu.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppSwitchTokens.Gap
import ru.bgitu.core.designsystem.components.AppSwitchTokens.TrackCornerRadius
import ru.bgitu.core.designsystem.components.AppSwitchTokens.TrackHeight
import ru.bgitu.core.designsystem.components.AppSwitchTokens.TrackWidth
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val thumbRadius = (TrackHeight / 2) - Gap

    val animatePosition by animateFloatAsState(
        targetValue = if (checked)
            with(LocalDensity.current) { (TrackWidth - thumbRadius - Gap).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + Gap).toPx() },
        label = "",
        animationSpec = tween()
    )

    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled -> AppTheme.colorScheme.backgroundDisabled.copy(.5f)
            checked -> AppTheme.colorScheme.backgroundBrand
            else -> AppTheme.colorScheme.backgroundDisabled
        },
        animationSpec = tween()
    )
    val thumbColor = Color.White

    Canvas(
        modifier = modifier
            .size(width = TrackWidth, height = TrackHeight)
            .pointerInput(checked) {
                detectTapGestures(
                    onTap = { onCheckedChange(!checked) }
                )
            }
    ) {
        // Track
        drawRoundRect(
            color = trackColor,
            cornerRadius = TrackCornerRadius,
        )

        // Thumb
        drawCircle(
            color = thumbColor,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition,
                y = size.height / 2
            )
        )
    }
}

object AppSwitchTokens {
    val TrackWidth = 50.dp
    val TrackHeight = 30.dp
    val Gap = 4.dp
    val TrackCornerRadius = CornerRadius(x = 100f, y = 100f)
}

@Preview
@Composable
private fun AppSwitchPreview() {
    CompassTheme {
        Column {
            AppSwitch(checked = true, onCheckedChange = {})
            Spacer(Modifier.height(AppTheme.spacing.l))
            AppSwitch(checked = false, onCheckedChange = {})
        }
    }
}
