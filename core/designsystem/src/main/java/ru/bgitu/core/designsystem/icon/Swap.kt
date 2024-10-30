package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Swap: ImageVector
    get() {
        if (_Swap != null) {
            return _Swap!!
        }
        _Swap = ImageVector.Builder(
            name = "Swap",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF808B9F)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(7.674f, 5.07f)
                curveTo(8.103f, 5.07f, 8.45f, 5.41f, 8.45f, 5.83f)
                verticalLineTo(19.24f)
                curveTo(8.45f, 19.545f, 8.264f, 19.821f, 7.977f, 19.94f)
                curveTo(7.69f, 20.059f, 7.358f, 19.998f, 7.134f, 19.786f)
                lineTo(2.736f, 15.616f)
                curveTo(2.428f, 15.324f, 2.42f, 14.843f, 2.719f, 14.541f)
                curveTo(3.017f, 14.239f, 3.509f, 14.232f, 3.817f, 14.524f)
                lineTo(6.897f, 17.445f)
                verticalLineTo(5.83f)
                curveTo(6.897f, 5.41f, 7.245f, 5.07f, 7.674f, 5.07f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF808B9F)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(12.342f, 14.977f)
                curveTo(11.913f, 14.977f, 11.565f, 14.637f, 11.565f, 14.217f)
                lineTo(11.565f, 0.76f)
                curveTo(11.565f, 0.454f, 11.753f, 0.178f, 12.042f, 0.059f)
                curveTo(12.33f, -0.059f, 12.663f, 0.003f, 12.887f, 0.218f)
                lineTo(17.268f, 4.435f)
                curveTo(17.574f, 4.73f, 17.578f, 5.211f, 17.277f, 5.51f)
                curveTo(16.976f, 5.81f, 16.485f, 5.814f, 16.179f, 5.52f)
                lineTo(13.119f, 2.574f)
                lineTo(13.119f, 14.217f)
                curveTo(13.119f, 14.637f, 12.771f, 14.977f, 12.342f, 14.977f)
                close()
            }
        }.build()

        return _Swap!!
    }

@Suppress("ObjectPropertyName")
private var _Swap: ImageVector? = null
