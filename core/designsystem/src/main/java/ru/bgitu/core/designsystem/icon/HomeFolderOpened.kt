package ru.bgitu.core.designsystem.icon

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.theme.LocalNewColorScheme

@Composable
fun AppIcons.HomeFolderOpened(tint: Color = LocalContentColor.current): ImageVector {
    if (_HomeFolderOpened != null && tint.value == _key) {
        _key = tint.value
        return _HomeFolderOpened!!
    }
    _HomeFolderOpened = ImageVector.Builder(
        name = "HomeFolderOpened",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(tint),
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
        path(
            fill = SolidColor(LocalNewColorScheme.current.background3),
            strokeLineWidth = 0.444445f
        ) {
            moveToRelative(12.951f, 13.227f)
            arcToRelative(0.889f, 0.889f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.067f, 0f)
            lineToRelative(-3.111f, 2.333f)
            arcToRelative(0.889f, 0.889f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.356f, 0.711f)
            verticalLineToRelative(4.222f)
            arcToRelative(0.889f, 0.889f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.889f, 0.889f)
            horizontalLineToRelative(1.733f)
            arcToRelative(0.489f, 0.489f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.489f, -0.489f)
            verticalLineToRelative(-2.178f)
            arcToRelative(0.889f, 0.889f, 0f, isMoreThanHalf = true, isPositiveArc = true, 1.778f, 0f)
            verticalLineToRelative(2.178f)
            arcToRelative(0.489f, 0.489f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.489f, 0.489f)
            horizontalLineToRelative(1.733f)
            arcToRelative(0.889f, 0.889f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.889f, -0.889f)
            verticalLineToRelative(-4.222f)
            arcToRelative(0.889f, 0.889f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.356f, -0.711f)
            close()
        }
    }.build()

    return _HomeFolderOpened!!
}

@Suppress("ObjectPropertyName")
private var _HomeFolderOpened: ImageVector? = null

@Suppress("ObjectPropertyName")
private var _key: Any? = null