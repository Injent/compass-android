package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Building: ImageVector
    get() {
        if (_Building != null) {
            return _Building!!
        }
        _Building = ImageVector.Builder(
            name = "Building",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF272E3A)),
                strokeLineWidth = 1.10105f
            ) {
                moveToRelative(14.23f, 7.54f)
                horizontalLineToRelative(1.101f)
                arcToRelative(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -2.202f)
                horizontalLineToRelative(-1.101f)
                arcToRelative(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 2.202f)
                close()
                moveTo(14.23f, 11.944f)
                horizontalLineToRelative(1.101f)
                arcToRelative(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -2.202f)
                horizontalLineToRelative(-1.101f)
                arcToRelative(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 2.202f)
                close()
                moveTo(8.725f, 7.54f)
                horizontalLineToRelative(1.101f)
                arcToRelative(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -2.202f)
                lineTo(8.725f, 5.338f)
                arcToRelative(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 2.202f)
                close()
                moveTo(8.725f, 11.944f)
                horizontalLineToRelative(1.101f)
                arcToRelative(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -2.202f)
                lineTo(8.725f, 9.742f)
                arcToRelative(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 2.202f)
                close()
                moveTo(21.938f, 20.752f)
                lineTo(20.836f, 20.752f)
                lineTo(20.836f, 2.034f)
                arcTo(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 19.735f, 0.933f)
                lineTo(4.321f, 0.933f)
                arcTo(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.22f, 2.034f)
                lineTo(3.22f, 20.752f)
                lineTo(2.119f, 20.752f)
                arcToRelative(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 2.202f)
                lineTo(21.938f, 22.954f)
                arcToRelative(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -2.202f)
                close()
                moveTo(13.129f, 20.752f)
                horizontalLineToRelative(-2.202f)
                verticalLineToRelative(-4.404f)
                horizontalLineToRelative(2.202f)
                close()
                moveTo(18.634f, 20.752f)
                lineTo(15.331f, 20.752f)
                lineTo(15.331f, 15.247f)
                arcTo(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 14.23f, 14.146f)
                lineTo(9.826f, 14.146f)
                arcTo(1.101f, 1.101f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8.725f, 15.247f)
                lineTo(8.725f, 20.752f)
                lineTo(5.422f, 20.752f)
                lineTo(5.422f, 3.135f)
                lineTo(18.634f, 3.135f)
                close()
            }
        }.build()

        return _Building!!
    }

@Suppress("ObjectPropertyName")
private var _Building: ImageVector? = null
