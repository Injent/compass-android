package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.FolderOpen: ImageVector
    get() {
        if (_FolderOpen != null) {
            return _FolderOpen!!
        }
        _FolderOpen = ImageVector.Builder(
            name = "FolderOpen",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.09845f
            ) {
                moveToRelative(20.54f, 9.591f)
                curveToRelative(0.42f, 0f, 0.823f, 0.063f, 1.202f, 0.178f)
                verticalLineTo(9.591f)
                arcTo(3.295f, 3.295f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.446f, 6.295f)
                horizontalLineTo(14.185f)
                arcTo(0.824f, 0.824f, 0f, isMoreThanHalf = false, isPositiveArc = true, 13.603f, 6.054f)
                lineTo(11.273f, 3.725f)
                arcTo(2.472f, 2.472f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.526f, 3f)
                horizontalLineTo(5.265f)
                arcTo(3.295f, 3.295f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.969f, 6.295f)
                verticalLineTo(9.769f)
                arcTo(4.126f, 4.126f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.171f, 9.591f)
                close()
                moveTo(3.171f, 11.238f)
                arcToRelative(2.472f, 2.472f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.446f, 2.821f)
                lineToRelative(0.941f, 6.591f)
                arcToRelative(2.472f, 2.472f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.446f, 2.122f)
                horizontalLineTo(19.6f)
                arcToRelative(2.472f, 2.472f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.446f, -2.122f)
                lineToRelative(0.941f, -6.591f)
                arcTo(2.472f, 2.472f, 0f, isMoreThanHalf = false, isPositiveArc = false, 20.541f, 11.238f)
                close()
            }
        }.build()

        return _FolderOpen!!
    }

@Suppress("ObjectPropertyName")
private var _FolderOpen: ImageVector? = null
