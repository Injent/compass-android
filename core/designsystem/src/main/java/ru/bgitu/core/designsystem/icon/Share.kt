package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Share: ImageVector
    get() {
        if (_Share != null) {
            return _Share!!
        }
        _Share = ImageVector.Builder(
            name = "Share",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(7.217f, 10.907f)
                arcToRelative(2.25f, 2.25f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0f, 2.186f)
                moveToRelative(0f, -2.186f)
                curveToRelative(0.18f, 0.324f, 0.283f, 0.696f, 0.283f, 1.093f)
                reflectiveCurveToRelative(-0.103f, 0.77f, -0.283f, 1.093f)
                moveToRelative(0f, -2.186f)
                lineToRelative(9.566f, -5.314f)
                moveToRelative(-9.566f, 7.5f)
                lineToRelative(9.566f, 5.314f)
                moveToRelative(0f, 0f)
                arcToRelative(2.25f, 2.25f, 0f, isMoreThanHalf = true, isPositiveArc = false, 3.935f, 2.186f)
                arcToRelative(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.935f, -2.186f)
                close()
                moveTo(16.783f, 5.593f)
                arcToRelative(2.25f, 2.25f, 0f, isMoreThanHalf = true, isPositiveArc = false, 3.933f, -2.185f)
                arcToRelative(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.933f, 2.185f)
                close()
            }
        }.build()

        return _Share!!
    }

@Suppress("ObjectPropertyName")
private var _Share: ImageVector? = null
