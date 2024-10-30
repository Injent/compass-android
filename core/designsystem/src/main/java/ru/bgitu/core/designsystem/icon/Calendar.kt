package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Calendar: ImageVector
    get() {
        if (_Calendar != null) {
            return _Calendar!!
        }
        _Calendar = ImageVector.Builder(
            name = "Calendar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            path(fill = SolidColor(Color(0xFF808B9F))) {
                moveTo(5.833f, 0f)
                curveTo(6.179f, 0f, 6.458f, 0.28f, 6.458f, 0.626f)
                verticalLineTo(4.802f)
                curveTo(6.458f, 5.148f, 6.179f, 5.428f, 5.833f, 5.428f)
                curveTo(5.488f, 5.428f, 5.208f, 5.148f, 5.208f, 4.802f)
                verticalLineTo(0.626f)
                curveTo(5.208f, 0.28f, 5.488f, 0f, 5.833f, 0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF808B9F)),
                strokeLineWidth = 0.962082f
            ) {
                moveTo(12.708f, 2.213f)
                horizontalLineTo(7.292f)
                verticalLineToRelative(2.551f)
                curveToRelative(0f, 0.747f, -0.653f, 1.353f, -1.458f, 1.353f)
                curveTo(5.028f, 6.116f, 4.375f, 5.511f, 4.375f, 4.764f)
                verticalLineTo(2.213f)
                horizontalLineTo(3.333f)
                curveTo(1.492f, 2.213f, 0f, 3.597f, 0f, 5.305f)
                verticalLineTo(7.971f)
                horizontalLineTo(20f)
                verticalLineTo(5.305f)
                curveTo(20f, 3.597f, 18.508f, 2.213f, 16.667f, 2.213f)
                horizontalLineTo(15.625f)
                verticalLineToRelative(2.551f)
                curveToRelative(0f, 0.747f, -0.653f, 1.353f, -1.458f, 1.353f)
                curveToRelative(-0.805f, 0f, -1.458f, -0.606f, -1.458f, -1.353f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF808B9F)),
                strokeLineWidth = 1.11609f
            ) {
                moveTo(20f, 7.153f)
                horizontalLineTo(0f)
                verticalLineTo(16.667f)
                curveTo(0f, 18.965f, 1.492f, 20f, 3.333f, 20f)
                horizontalLineTo(16.667f)
                curveTo(18.508f, 20f, 20f, 18.965f, 20f, 16.667f)
                close()
            }
            path(fill = SolidColor(Color(0xFF808B9F))) {
                moveTo(14.792f, 0.626f)
                curveTo(14.792f, 0.28f, 14.512f, 0f, 14.167f, 0f)
                curveTo(13.821f, 0f, 13.542f, 0.28f, 13.542f, 0.626f)
                verticalLineTo(4.802f)
                curveTo(13.542f, 5.148f, 13.821f, 5.428f, 14.167f, 5.428f)
                curveTo(14.512f, 5.428f, 14.792f, 5.148f, 14.792f, 4.802f)
                verticalLineTo(0.626f)
                close()
            }
        }.build()

        return _Calendar!!
    }

@Suppress("ObjectPropertyName")
private var _Calendar: ImageVector? = null
