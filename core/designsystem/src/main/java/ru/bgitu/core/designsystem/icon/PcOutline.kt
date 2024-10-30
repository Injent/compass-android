package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.PcOutline: ImageVector
    get() {
        if (_PcOutline != null) {
            return _PcOutline!!
        }
        _PcOutline = ImageVector.Builder(
            name = "PcOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.11111f
            ) {
                moveTo(19.778f, 0f)
                arcTo(2.222f, 2.222f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22f, 2.222f)
                verticalLineToRelative(12.222f)
                arcToRelative(2.222f, 2.222f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.222f, 2.222f)
                horizontalLineToRelative(-4.444f)
                verticalLineToRelative(1.111f)
                horizontalLineToRelative(1.111f)
                arcToRelative(1.111f, 1.111f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 2.222f)
                lineTo(7.556f, 20f)
                arcToRelative(1.111f, 1.111f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -2.222f)
                lineTo(8.667f, 17.778f)
                lineTo(8.667f, 16.667f)
                lineTo(4.222f, 16.667f)
                arcTo(2.222f, 2.222f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 14.444f)
                lineTo(2f, 2.222f)
                arcTo(2.222f, 2.222f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.222f, 0f)
                close()
                moveTo(13.111f, 16.667f)
                horizontalLineToRelative(-2.222f)
                verticalLineToRelative(1.111f)
                horizontalLineToRelative(2.222f)
                close()
                moveTo(19.778f, 2.222f)
                lineTo(4.222f, 2.222f)
                lineTo(4.222f, 14.444f)
                lineTo(19.778f, 14.444f)
                close()
            }
        }.build()

        return _PcOutline!!
    }

@Suppress("ObjectPropertyName")
private var _PcOutline: ImageVector? = null
