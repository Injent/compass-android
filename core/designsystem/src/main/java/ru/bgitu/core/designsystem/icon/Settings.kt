package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Settings: ImageVector
    get() {
        if (_Settings != null) {
            return _Settings!!
        }
        _Settings = ImageVector.Builder(
            name = "Settings",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.23077f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(11.788f, 0f)
                curveTo(10.661f, 0f, 9.697f, 0.816f, 9.511f, 1.929f)
                lineToRelative(-0.112f, 0.676f)
                arcTo(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.763f, 3.353f)
                arcTo(9.169f, 9.169f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8.175f, 3.596f)
                arcTo(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.195f, 3.518f)
                lineTo(6.638f, 3.119f)
                arcTo(2.308f, 2.308f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.664f, 3.365f)
                lineTo(3.365f, 3.664f)
                arcTo(2.308f, 2.308f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.119f, 6.638f)
                lineTo(3.518f, 7.195f)
                arcToRelative(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.079f, 0.98f)
                arcToRelative(9.167f, 9.167f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.244f, 0.588f)
                arcToRelative(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.748f, 0.636f)
                lineToRelative(-0.677f, 0.113f)
                arcTo(2.308f, 2.308f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 11.788f)
                verticalLineToRelative(0.423f)
                curveToRelative(0f, 1.127f, 0.816f, 2.091f, 1.929f, 2.277f)
                lineToRelative(0.676f, 0.112f)
                curveToRelative(0.346f, 0.058f, 0.625f, 0.308f, 0.748f, 0.636f)
                curveToRelative(0.074f, 0.199f, 0.156f, 0.395f, 0.244f, 0.588f)
                arcToRelative(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.079f, 0.98f)
                lineTo(3.119f, 17.362f)
                arcTo(2.308f, 2.308f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.365f, 20.336f)
                lineTo(3.664f, 20.635f)
                curveToRelative(0.798f, 0.798f, 2.055f, 0.902f, 2.974f, 0.246f)
                lineToRelative(0.558f, -0.399f)
                arcToRelative(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.98f, -0.079f)
                curveToRelative(0.193f, 0.087f, 0.389f, 0.169f, 0.588f, 0.244f)
                curveToRelative(0.329f, 0.123f, 0.578f, 0.402f, 0.636f, 0.748f)
                lineToRelative(0.113f, 0.677f)
                curveTo(9.697f, 23.184f, 10.66f, 24f, 11.788f, 24f)
                horizontalLineToRelative(0.423f)
                curveToRelative(1.127f, 0f, 2.091f, -0.816f, 2.277f, -1.929f)
                lineToRelative(0.112f, -0.676f)
                arcToRelative(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.636f, -0.748f)
                arcToRelative(9.255f, 9.255f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.588f, -0.244f)
                arcToRelative(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.98f, 0.079f)
                lineToRelative(0.558f, 0.399f)
                arcTo(2.308f, 2.308f, 0f, isMoreThanHalf = false, isPositiveArc = false, 20.336f, 20.635f)
                lineTo(20.635f, 20.336f)
                curveToRelative(0.798f, -0.798f, 0.902f, -2.055f, 0.246f, -2.974f)
                lineToRelative(-0.399f, -0.558f)
                arcToRelative(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.079f, -0.98f)
                curveToRelative(0.087f, -0.193f, 0.169f, -0.389f, 0.244f, -0.588f)
                curveToRelative(0.123f, -0.329f, 0.402f, -0.578f, 0.748f, -0.636f)
                lineToRelative(0.677f, -0.112f)
                arcTo(2.308f, 2.308f, 0f, isMoreThanHalf = false, isPositiveArc = false, 24f, 12.212f)
                verticalLineTo(11.788f)
                curveTo(24f, 10.661f, 23.184f, 9.697f, 22.071f, 9.511f)
                lineToRelative(-0.676f, -0.112f)
                arcToRelative(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.748f, -0.636f)
                arcToRelative(9.239f, 9.239f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.244f, -0.588f)
                arcToRelative(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.079f, -0.98f)
                lineToRelative(0.399f, -0.558f)
                arcTo(2.308f, 2.308f, 0f, isMoreThanHalf = false, isPositiveArc = false, 20.635f, 3.664f)
                lineTo(20.336f, 3.365f)
                arcTo(2.308f, 2.308f, 0f, isMoreThanHalf = false, isPositiveArc = false, 17.362f, 3.119f)
                lineTo(16.805f, 3.518f)
                arcTo(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, 15.825f, 3.596f)
                arcTo(9.184f, 9.184f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.237f, 3.353f)
                arcTo(0.982f, 0.982f, 0f, isMoreThanHalf = false, isPositiveArc = true, 14.601f, 2.604f)
                lineToRelative(-0.112f, -0.677f)
                arcTo(2.308f, 2.308f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.212f, 0f)
                close()
                moveTo(12f, 16.615f)
                arcToRelative(4.615f, 4.615f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0f, -9.231f)
                arcToRelative(4.615f, 4.615f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 9.231f)
                close()
            }
        }.build()

        return _Settings!!
    }

@Suppress("ObjectPropertyName")
private var _Settings: ImageVector? = null
