package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Folder: ImageVector
    get() {
        if (_FolderFill != null) {
            return _FolderFill!!
        }
        _FolderFill = ImageVector.Builder(
            name = "FolderFill",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
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
        }.build()

        return _FolderFill!!
    }

@Suppress("ObjectPropertyName")
private var _FolderFill: ImageVector? = null

val AppIcons.HomeInFolder: ImageVector
    get() {
        if (_HomeInFolder != null) {
            return _HomeInFolder!!
        }
        _HomeInFolder = ImageVector.Builder(
            name = "HomeInFolder",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 0.556f
            ) {
                moveToRelative(13.129f, 10.214f)
                arcToRelative(1.111f, 1.111f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.333f, 0f)
                lineTo(7.907f, 13.13f)
                arcToRelative(1.111f, 1.111f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.444f, 0.889f)
                lineToRelative(0f, 5.278f)
                arcToRelative(1.111f, 1.111f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.111f, 1.111f)
                lineToRelative(2.167f, 0f)
                arcToRelative(0.611f, 0.611f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.611f, -0.611f)
                lineToRelative(0f, -2.722f)
                arcToRelative(1.111f, 1.111f, 0f, isMoreThanHalf = true, isPositiveArc = true, 2.222f, 0f)
                lineToRelative(0f, 2.722f)
                arcToRelative(0.611f, 0.611f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.611f, 0.611f)
                lineToRelative(2.167f, 0f)
                arcToRelative(1.111f, 1.111f, 135f, isMoreThanHalf = false, isPositiveArc = false, 1.111f, -1.111f)
                lineToRelative(0f, -5.278f)
                arcToRelative(1.111f, 1.111f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.444f, -0.889f)
                close()
            }
        }.build()

        return _HomeInFolder!!
    }

@Suppress("ObjectPropertyName")
private var _HomeInFolder: ImageVector? = null