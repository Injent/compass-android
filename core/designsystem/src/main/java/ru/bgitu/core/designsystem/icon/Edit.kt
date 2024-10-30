package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Edit: ImageVector
    get() {
        if (_Edit != null) {
            return _Edit!!
        }
        _Edit = ImageVector.Builder(
            name = "Edit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.77482f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveToRelative(17.289f, 3.575f)
                lineToRelative(1.996f, -1.997f)
                arcToRelative(2.219f, 2.219f, 0f, isMoreThanHalf = true, isPositiveArc = true, 3.138f, 3.138f)
                lineTo(9.859f, 17.28f)
                arcToRelative(5.324f, 5.324f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.245f, 1.337f)
                lineToRelative(-3.177f, 0.947f)
                lineToRelative(0.947f, -3.177f)
                arcToRelative(5.324f, 5.324f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.337f, -2.245f)
                close()
                moveTo(17.289f, 3.575f)
                lineTo(20.41f, 6.696f)
                moveToRelative(-1.775f, 8.135f)
                verticalLineToRelative(5.62f)
                arcTo(2.662f, 2.662f, 0f, isMoreThanHalf = false, isPositiveArc = true, 15.973f, 23.113f)
                lineTo(3.55f, 23.113f)
                arcTo(2.662f, 2.662f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.887f, 20.451f)
                lineTo(0.887f, 8.027f)
                arcTo(2.662f, 2.662f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.55f, 5.365f)
                horizontalLineToRelative(5.62f)
            }
        }.build()

        return _Edit!!
    }

@Suppress("ObjectPropertyName")
private var _Edit: ImageVector? = null
