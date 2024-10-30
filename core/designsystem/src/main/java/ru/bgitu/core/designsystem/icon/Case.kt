package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Case: ImageVector
    get() {
        if (_Case != null) {
            return _Case!!
        }
        _Case = ImageVector.Builder(
            name = "Case",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.1282f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(6.923f, 4.385f)
                arcTo(3.385f, 3.385f, 0f, isMoreThanHalf = false, isPositiveArc = true, 10.308f, 1f)
                horizontalLineToRelative(3.385f)
                arcToRelative(3.385f, 3.385f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.385f, 3.385f)
                verticalLineToRelative(0.231f)
                curveTo(18.13f, 4.712f, 19.172f, 4.838f, 20.207f, 4.993f)
                curveTo(21.847f, 5.239f, 23f, 6.666f, 23f, 8.284f)
                verticalLineToRelative(3.422f)
                curveToRelative(0f, 1.366f, -0.828f, 2.654f, -2.184f, 3.105f)
                arcTo(27.896f, 27.896f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 16.231f)
                curveToRelative(-3.08f, 0f, -6.044f, -0.499f, -8.816f, -1.42f)
                curveTo(1.828f, 14.359f, 1f, 13.072f, 1f, 11.706f)
                lineTo(1f, 8.284f)
                curveTo(1f, 6.666f, 2.153f, 5.238f, 3.793f, 4.993f)
                arcTo(55.072f, 55.072f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6.923f, 4.616f)
                close()
                moveTo(15.385f, 4.385f)
                verticalLineToRelative(0.102f)
                arcToRelative(55.833f, 55.833f, 0f, isMoreThanHalf = false, isPositiveArc = false, -6.769f, 0f)
                lineTo(8.615f, 4.385f)
                arcTo(1.692f, 1.692f, 0f, isMoreThanHalf = false, isPositiveArc = true, 10.308f, 2.692f)
                horizontalLineToRelative(3.385f)
                arcToRelative(1.692f, 1.692f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.692f, 1.692f)
                close()
                moveTo(12f, 13.692f)
                arcToRelative(0.846f, 0.846f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0f, -1.692f)
                arcToRelative(0.846f, 0.846f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 1.692f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.1282f
            ) {
                moveTo(1.846f, 19.221f)
                verticalLineTo(16.066f)
                arcTo(4.851f, 4.851f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.651f, 16.416f)
                arcTo(29.588f, 29.588f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 17.923f)
                curveToRelative(3.263f, 0f, 6.408f, -0.528f, 9.349f, -1.506f)
                curveToRelative(0.284f, -0.095f, 0.553f, -0.213f, 0.804f, -0.351f)
                verticalLineToRelative(3.154f)
                curveToRelative(0f, 1.638f, -1.181f, 3.078f, -2.846f, 3.298f)
                curveTo(16.916f, 22.836f, 14.476f, 23f, 12f, 23f)
                arcTo(55.496f, 55.496f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.693f, 22.518f)
                curveToRelative(-1.665f, -0.22f, -2.846f, -1.66f, -2.846f, -3.298f)
                close()
            }
        }.build()

        return _Case!!
    }

@Suppress("ObjectPropertyName")
private var _Case: ImageVector? = null
