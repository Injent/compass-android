package ru.bgitu.core.designsystem.util

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme

@Composable
fun Modifier.shimmer(
    durationMillis: Int = 1200,
    delayMillis: Int = 0,
    highlightColor: Color = Color.White,
    baseColor: Color = Color(0x3397A4BA),
    shape: Shape = RectangleShape
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val startOffsetX by infiniteTransition.animateFloat(
        initialValue = -size.width.toFloat() * 1.5f,
        targetValue = size.width.toFloat() * 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, delayMillis = delayMillis),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(baseColor, highlightColor, baseColor),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), 40f)
        ),
        shape = shape
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Preview(showBackground = true)
@Composable
private fun ShimmerPreview() {
    CompassTheme {
        Box(Modifier.padding(AppTheme.spacing.xl)) {
            Spacer(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shimmer()
            )
        }
    }
}