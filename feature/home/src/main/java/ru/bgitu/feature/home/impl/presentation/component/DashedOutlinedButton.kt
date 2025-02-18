package ru.bgitu.feature.home.impl.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.SoftwareLayer

@Composable
fun DashedOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val density = LocalDensity.current
    val shape = AppTheme.shapes.default
    val stroke = Stroke(
        width = density.run { AppTheme.strokeWidth.thin.toPx() },
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    val borderColor = AppTheme.colorScheme.brandStroke

    SoftwareLayer(modifier) {
        AppTextButton(
            text = text,
            onClick = onClick,
            shape = shape,
            modifier = Modifier
                .then(
                    if (isLandscape) {
                        Modifier
                            .wrapContentWidth()
                    } else {
                        Modifier.fillMaxWidth()
                    }
                )
                .drawBehind {
                    drawRoundRect(
                        color = borderColor,
                        style = stroke,
                        cornerRadius = CornerRadius(shape.topStart.toPx(size, density))
                    )
                }
        )
    }
}