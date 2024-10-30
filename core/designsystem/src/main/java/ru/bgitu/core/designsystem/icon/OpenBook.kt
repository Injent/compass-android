package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.OpenBook: ImageVector
    get() {
        if (_OpenBook != null) {
            return _OpenBook!!
        }
        _OpenBook = ImageVector.Builder(
            name = "OpenBook",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(10.945f, 20.538f)
                arcTo(9.522f, 9.522f, 45f, isMoreThanHalf = false, isPositiveArc = false, 9.922f, 19.99f)
                curveTo(9.042f, 19.583f, 7.868f, 19.2f, 6.6f, 19.2f)
                curveTo(5.065f, 19.2f, 3.674f, 19.762f, 2.784f, 20.234f)
                arcTo(1.91f, 1.91f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.967f, 20.21f)
                arcTo(1.841f, 1.841f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 18.6f)
                lineTo(0f, 3.6f)
                curveTo(0f, 2.855f, 0.354f, 2.084f, 1.078f, 1.645f)
                curveTo(2.006f, 1.081f, 4.097f, 0f, 6.6f, 0f)
                curveToRelative(1.897f, 0f, 3.774f, 0.612f, 5.4f, 1.572f)
                curveToRelative(1.626f, -0.96f, 3.503f, -1.572f, 5.4f, -1.572f)
                curveToRelative(2.503f, 0f, 4.594f, 1.08f, 5.522f, 1.645f)
                curveTo(23.646f, 2.084f, 24f, 2.855f, 24f, 3.6f)
                lineTo(24f, 18.6f)
                curveToRelative(0f, 0.76f, -0.455f, 1.327f, -0.967f, 1.61f)
                arcToRelative(1.91f, 1.91f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.818f, 0.024f)
                curveTo(20.326f, 19.762f, 18.935f, 19.2f, 17.4f, 19.2f)
                curveToRelative(-1.268f, 0f, -2.442f, 0.384f, -3.322f, 0.79f)
                arcToRelative(9.522f, 9.522f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.024f, 0.548f)
                curveTo(12.714f, 20.75f, 12.426f, 21f, 12.001f, 21f)
                curveTo(11.574f, 21f, 11.287f, 20.75f, 10.945f, 20.538f)
                moveTo(2.4f, 17.753f)
                lineTo(2.4f, 3.65f)
                curveTo(3.208f, 3.17f, 4.8f, 2.4f, 6.6f, 2.4f)
                curveTo(8.076f, 2.4f, 9.538f, 2.902f, 10.8f, 3.65f)
                lineTo(10.8f, 17.753f)
                curveTo(9.738f, 17.278f, 8.264f, 16.8f, 6.6f, 16.8f)
                curveTo(4.943f, 16.8f, 3.469f, 17.274f, 2.4f, 17.753f)
                moveToRelative(10.8f, 0f)
                curveToRelative(1.062f, -0.475f, 2.536f, -0.953f, 4.2f, -0.953f)
                curveToRelative(1.657f, 0f, 3.131f, 0.474f, 4.2f, 0.953f)
                lineTo(21.6f, 3.65f)
                curveTo(20.792f, 3.17f, 19.2f, 2.4f, 17.4f, 2.4f)
                curveTo(15.924f, 2.4f, 14.462f, 2.902f, 13.2f, 3.65f)
                close()
            }
        }.build()

        return _OpenBook!!
    }

@Suppress("ObjectPropertyName")
private var _OpenBook: ImageVector? = null
