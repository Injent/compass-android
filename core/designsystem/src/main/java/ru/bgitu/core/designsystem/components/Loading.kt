package ru.bgitu.core.designsystem.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.bgitu.core.designsystem.R
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme

@Composable
fun AppCircularLoading(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 720f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing =  FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(offsetMillis = 700, StartOffsetType.FastForward)
        ), label = ""
    )

    Icon(
        painter = painterResource(R.drawable.ic_loading),
        contentDescription = null,
        tint = tint,
        modifier = modifier.rotate(rotation)
    )
}

@Composable
fun AppDoubleLoading(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Box(modifier) {
        AppCircularLoading(
            tint = tint
        )

    }
}

@Preview
@Composable
private fun AppDoubleLoadingPreview() {
    CompassTheme {
        AppDoubleLoading(
            tint = AppTheme.colorScheme.foreground
        )
    }
}