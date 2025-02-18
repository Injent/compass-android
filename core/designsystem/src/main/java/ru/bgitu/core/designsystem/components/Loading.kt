package ru.bgitu.core.designsystem.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.bgitu.core.common.CommonDrawables
import ru.bgitu.core.designsystem.R
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme

@Composable
fun CompassLoading(
    modifier: Modifier = Modifier,
    loadingSize: Dp = 48.dp,
) {
    val arrowPainter = painterResource(CommonDrawables.ic_compass_arrow)

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val additionalRotation by infiniteTransition.animateFloat(
        initialValue = 120f,
        targetValue = -60f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                2000,
                easing = LinearOutSlowInEasing
            )
        )
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = -120f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(offsetMillis = 0, StartOffsetType.FastForward)
        ),
    )

    Box(
        contentAlignment = Alignment.Center
    ) {
        AppCircularLoading(
            modifier = Modifier.size(loadingSize),
            tint = AppTheme.colorScheme.foreground
        )
        Icon(
            painter = arrowPainter,
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground,
            modifier = modifier
                .size(loadingSize)
                .padding(loadingSize / 6)
                .rotate(rotation - additionalRotation)
        )
    }
}

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
            animation = tween(2000, easing =  FastOutSlowInEasing),
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

@Preview
@Composable
private fun CompassLoadingPreview() {
    CompassTheme {
        CompassLoading(loadingSize = 48.dp)
    }
}