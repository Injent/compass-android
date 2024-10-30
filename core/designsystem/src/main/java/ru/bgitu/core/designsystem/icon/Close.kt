package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Close: ImageVector
    get() {
        if (_Close != null) {
            return _Close!!
        }
        _Close = ImageVector.Builder(
            name = "Close",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            group {
                path(fill = SolidColor(Color(0xFF000000))) {
                    moveTo(1.722f, 0.295f)
                    curveTo(1.328f, -0.099f, 0.689f, -0.099f, 0.295f, 0.295f)
                    curveTo(-0.098f, 0.689f, -0.098f, 1.328f, 0.295f, 1.721f)
                    lineTo(4.574f, 6f)
                    lineTo(0.295f, 10.278f)
                    curveTo(-0.098f, 10.672f, -0.098f, 11.311f, 0.295f, 11.705f)
                    curveTo(0.689f, 12.098f, 1.328f, 12.098f, 1.722f, 11.705f)
                    lineTo(6f, 7.426f)
                    lineTo(10.278f, 11.705f)
                    curveTo(10.672f, 12.098f, 11.311f, 12.098f, 11.705f, 11.705f)
                    curveTo(12.099f, 11.311f, 12.099f, 10.672f, 11.705f, 10.278f)
                    lineTo(7.426f, 6f)
                    lineTo(11.705f, 1.721f)
                    curveTo(12.099f, 1.328f, 12.099f, 0.689f, 11.705f, 0.295f)
                    curveTo(11.311f, -0.099f, 10.672f, -0.099f, 10.278f, 0.295f)
                    lineTo(6f, 4.574f)
                    lineTo(1.722f, 0.295f)
                    close()
                }
            }
        }.build()

        return _Close!!
    }

@Suppress("ObjectPropertyName")
private var _Close: ImageVector? = null
