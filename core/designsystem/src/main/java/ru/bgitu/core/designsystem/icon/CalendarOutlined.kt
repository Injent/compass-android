package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.CalendarOutlined: ImageVector
    get() {
        if (_CalendarOutline != null) {
            return _CalendarOutline!!
        }
        _CalendarOutline = ImageVector.Builder(
            name = "CalendarOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            path(fill = SolidColor(Color(0xFF185AC5))) {
                moveTo(16.651f, 2.333f)
                horizontalLineTo(14.857f)
                verticalLineTo(0.746f)
                curveTo(14.857f, 0.333f, 14.524f, 0f, 14.111f, 0f)
                curveTo(13.698f, 0f, 13.365f, 0.333f, 13.365f, 0.746f)
                verticalLineTo(2.333f)
                horizontalLineTo(6.651f)
                verticalLineTo(0.746f)
                curveTo(6.651f, 0.333f, 6.317f, 0f, 5.905f, 0f)
                curveTo(5.492f, 0f, 5.159f, 0.333f, 5.159f, 0.746f)
                verticalLineTo(2.333f)
                horizontalLineTo(3.349f)
                curveTo(1.508f, 2.333f, 0f, 3.841f, 0f, 5.683f)
                verticalLineTo(16.651f)
                curveTo(0f, 18.508f, 1.508f, 20f, 3.349f, 20f)
                horizontalLineTo(16.651f)
                curveTo(18.508f, 20f, 20f, 18.492f, 20f, 16.651f)
                verticalLineTo(5.683f)
                curveTo(20f, 3.841f, 18.508f, 2.333f, 16.651f, 2.333f)
                close()
                moveTo(3.349f, 3.841f)
                horizontalLineTo(5.143f)
                verticalLineTo(4.762f)
                curveTo(5.143f, 5.175f, 5.476f, 5.508f, 5.889f, 5.508f)
                curveTo(6.302f, 5.508f, 6.635f, 5.175f, 6.635f, 4.762f)
                verticalLineTo(3.841f)
                horizontalLineTo(13.333f)
                verticalLineTo(4.762f)
                curveTo(13.333f, 5.175f, 13.667f, 5.508f, 14.079f, 5.508f)
                curveTo(14.492f, 5.508f, 14.825f, 5.175f, 14.825f, 4.762f)
                verticalLineTo(3.841f)
                horizontalLineTo(16.619f)
                curveTo(17.635f, 3.841f, 18.476f, 4.667f, 18.476f, 5.698f)
                verticalLineTo(8.333f)
                horizontalLineTo(1.508f)
                verticalLineTo(5.683f)
                curveTo(1.508f, 4.667f, 2.333f, 3.841f, 3.349f, 3.841f)
                close()
                moveTo(16.651f, 18.508f)
                horizontalLineTo(3.349f)
                curveTo(2.333f, 18.508f, 1.492f, 17.683f, 1.492f, 16.651f)
                verticalLineTo(9.825f)
                horizontalLineTo(18.492f)
                verticalLineTo(16.667f)
                curveTo(18.508f, 17.667f, 17.683f, 18.508f, 16.651f, 18.508f)
                close()
            }
        }.build()

        return _CalendarOutline!!
    }

@Suppress("ObjectPropertyName")
private var _CalendarOutline: ImageVector? = null
