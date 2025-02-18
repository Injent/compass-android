package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Smartphone: ImageVector
    get() {
        if (_Smartphone != null) {
            return _Smartphone!!
        }
        _Smartphone = ImageVector.Builder(
            name = "Smartphone",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            path(fill = SolidColor(Color(0xFF808B9F))) {
                moveTo(11.146f, 16.198f)
                curveTo(11.146f, 16.802f, 10.656f, 17.292f, 10.052f, 17.292f)
                curveTo(9.448f, 17.292f, 8.958f, 16.802f, 8.958f, 16.198f)
                curveTo(8.958f, 15.594f, 9.448f, 15.104f, 10.052f, 15.104f)
                curveTo(10.656f, 15.104f, 11.146f, 15.594f, 11.146f, 16.198f)
                close()
            }
            path(fill = SolidColor(Color(0xFF808B9F))) {
                moveTo(8.229f, 2.813f)
                curveTo(7.884f, 2.813f, 7.604f, 3.092f, 7.604f, 3.438f)
                curveTo(7.604f, 3.783f, 7.884f, 4.063f, 8.229f, 4.063f)
                horizontalLineTo(11.875f)
                curveTo(12.22f, 4.063f, 12.5f, 3.783f, 12.5f, 3.438f)
                curveTo(12.5f, 3.092f, 12.22f, 2.813f, 11.875f, 2.813f)
                horizontalLineTo(8.229f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF808B9F)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(5.417f, 0f)
                horizontalLineTo(14.583f)
                curveTo(15.734f, 0f, 16.667f, 0.933f, 16.667f, 2.083f)
                verticalLineTo(17.917f)
                curveTo(16.667f, 19.067f, 15.734f, 20f, 14.583f, 20f)
                horizontalLineTo(5.417f)
                curveTo(4.266f, 20f, 3.333f, 19.067f, 3.333f, 17.917f)
                verticalLineTo(2.083f)
                curveTo(3.333f, 0.933f, 4.266f, 0f, 5.417f, 0f)
                close()
                moveTo(5.417f, 1.25f)
                curveTo(4.956f, 1.25f, 4.583f, 1.623f, 4.583f, 2.083f)
                verticalLineTo(17.917f)
                curveTo(4.583f, 18.377f, 4.956f, 18.75f, 5.417f, 18.75f)
                horizontalLineTo(14.583f)
                curveTo(15.043f, 18.75f, 15.417f, 18.377f, 15.417f, 17.917f)
                verticalLineTo(2.083f)
                curveTo(15.417f, 1.623f, 15.043f, 1.25f, 14.583f, 1.25f)
                horizontalLineTo(5.417f)
                close()
            }
        }.build()

        return _Smartphone!!
    }

@Suppress("ObjectPropertyName")
private var _Smartphone: ImageVector? = null
