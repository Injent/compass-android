package ru.bgitu.core.designsystem.util.shadow

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.theme.SpotCard
import ru.bgitu.core.designsystem.util.boxShadow

fun Modifier.basicShadow(shape: Shape = RectangleShape): Modifier {
    return if (SDK_INT < 28) {
        roundRectShadow(
            offset = DpOffset(0.dp, 2.dp),
            shape = shape,
            radius = 10.dp,
        )
    } else {
        boxShadow(
            color = SpotCard,
            blurRadius = 6.dp,
            spreadRadius = 0.dp,
            offset = DpOffset(0.dp, 2.dp),
            shape = shape,
            clip = false,
            inset = false,
            alpha = .5f
        )
    }
}

fun Modifier.roundRectShadow(
    color: Color = SpotCard,
    offset: DpOffset = DpOffset.Zero,
    shape: Shape = RectangleShape,
    radius: Dp,
    alpha: Float = 0.05f
) = this.then(
    ShadowDrawer(
        CustomShadowParams(
            name = "default",
            layers = listOf(
                Shadow(
                    dX = offset.x.value.toPx,
                    dY = offset.y.value.toPx,
                    radius = radius.value.toPx,
                    color = color.toArgb(),
                    colorAlpha = alpha,
                    linearGradientParams = GradientParams(
                        listOf(
                            GradientPointAndColorMultiplier(0f, 1f),
                            GradientPointAndColorMultiplier(0.85f, 0.1f),
                            GradientPointAndColorMultiplier(1f, 0f)
                        )
                    )
                )
            )
        ),
        shape,
    )
)

private class ShadowDrawer(
    private val customShadowParams: CustomShadowParams,
    private val shape: Shape,
) : DrawModifier {

    private val composeCompatShadowsRenderer = ComposeCompatShadowsRenderer()

    override fun ContentDrawScope.draw() {
        val density = Density(density, fontScale)

        val cornersParams = when (shape) {
            is RoundedCornerShape -> {
                CornersParams(
                    topLeft = shape.topStart.toPx(size, density),
                    topRight = shape.topEnd.toPx(size, density),
                    bottomLeft = shape.bottomStart.toPx(size, density),
                    bottomRight = shape.bottomEnd.toPx(size, density)
                )
            }
            else -> {
                drawContent()
                return
            }
        }

        customShadowParams.layers.forEach {
            composeCompatShadowsRenderer.paintCompatShadow(
                canvas = this,
                cornersParams = cornersParams,
                shadow = it
            )
        }
        drawContent()
    }
}