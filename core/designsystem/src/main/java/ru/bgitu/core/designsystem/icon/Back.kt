package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Back2: ImageVector
    get() {
        if (_AngleLeft != null) {
            return _AngleLeft!!
        }
        _AngleLeft = ImageVector.Builder(
            name = "AngleLeft",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(17.921f, 1.505f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.44f, 1.06f)
                lineTo(9.809f, 10.237f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 3.536f)
                lineToRelative(7.662f, 7.662f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.121f, 2.121f)
                lineTo(7.688f, 15.9f)
                arcToRelative(5.506f, 5.506f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -7.779f)
                lineTo(15.36f, 0.444f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.561f, 1.061f)
                close()
            }
        }.build()

        return _AngleLeft!!
    }

@Suppress("ObjectPropertyName")
private var _AngleLeft: ImageVector? = null
