package ru.bgitu.core.designsystem.icon

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.theme.LocalNewColorScheme

val AppIcons.HomeFolder: ImageVector
    @Composable get() {
        if (_HomeFolder != null) {
            return _HomeFolder!!
        }
        _HomeFolder = ImageVector.Builder(
            name = "HomeFolder",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(LocalContentColor.current)) {
                moveTo(19.5f, 21f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3f, -3f)
                verticalLineToRelative(-4.5f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3f, -3f)
                horizontalLineToRelative(-15f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3f, 3f)
                verticalLineTo(18f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3f, 3f)
                horizontalLineToRelative(15f)
                close()
                moveTo(1.5f, 10.146f)
                verticalLineTo(6f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3f, -3f)
                horizontalLineToRelative(5.379f)
                arcToRelative(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.59f, 0.659f)
                lineToRelative(2.122f, 2.121f)
                curveToRelative(0.14f, 0.141f, 0.331f, 0.22f, 0.53f, 0.22f)
                horizontalLineTo(19.5f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3f, 3f)
                verticalLineToRelative(1.146f)
                arcTo(4.483f, 4.483f, 0f, isMoreThanHalf = false, isPositiveArc = false, 19.5f, 9f)
                horizontalLineToRelative(-15f)
                arcToRelative(4.483f, 4.483f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3f, 1.146f)
                close()
            }
            path(
                fill = SolidColor(LocalNewColorScheme.current.background3),
                strokeLineWidth = 0.444445f
            ) {
                moveToRelative(12.653f, 12.456f)
                arcToRelative(0.778f, 0.778f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.933f, 0f)
                lineTo(8.997f, 14.497f)
                arcTo(0.778f, 0.778f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8.686f, 15.119f)
                verticalLineToRelative(3.694f)
                arcToRelative(0.778f, 0.778f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.778f, 0.778f)
                horizontalLineToRelative(1.517f)
                arcToRelative(0.428f, 0.428f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.428f, -0.428f)
                verticalLineToRelative(-1.906f)
                arcToRelative(0.778f, 0.778f, 0f, isMoreThanHalf = true, isPositiveArc = true, 1.556f, 0f)
                verticalLineToRelative(1.906f)
                arcToRelative(0.428f, 0.428f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.428f, 0.428f)
                horizontalLineToRelative(1.517f)
                arcTo(0.778f, 0.778f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.686f, 18.814f)
                verticalLineTo(15.119f)
                arcTo(0.778f, 0.778f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.375f, 14.497f)
                close()
            }
        }.build()

        return _HomeFolder!!
    }

@Suppress("ObjectPropertyName")
private var _HomeFolder: ImageVector? = null
