package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Vk: ImageVector
    get() {
        if (_Vk != null) {
            return _Vk!!
        }
        _Vk = ImageVector.Builder(
            name = "Vk",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0x00000000)),
                strokeAlpha = 0.352941f,
                strokeLineWidth = 0.140625f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2.309f, 4.765f)
                horizontalLineToRelative(20.141f)
                verticalLineToRelative(15.055f)
                horizontalLineToRelative(-20.141f)
                close()
            }
            path(fill = SolidColor(Color(0xFF0077FF))) {
                moveTo(22.316f, 1.684f)
                curveTo(20.632f, 0f, 17.921f, 0f, 12.5f, 0f)
                horizontalLineToRelative(-1f)
                curveTo(6.079f, 0f, 3.368f, 0f, 1.684f, 1.684f)
                curveTo(0f, 3.368f, 0f, 6.079f, 0f, 11.5f)
                verticalLineToRelative(1f)
                curveToRelative(0f, 5.421f, 0f, 8.131f, 1.684f, 9.816f)
                curveTo(3.368f, 24.001f, 6.079f, 24f, 11.5f, 24f)
                horizontalLineToRelative(1f)
                curveToRelative(5.421f, 0f, 8.131f, 0f, 9.816f, -1.684f)
                curveTo(24f, 20.632f, 24f, 17.921f, 24f, 12.5f)
                verticalLineToRelative(-1f)
                curveTo(24f, 6.079f, 24f, 3.368f, 22.316f, 1.684f)
                close()
                moveTo(19.503f, 17f)
                horizontalLineToRelative(-1.75f)
                curveTo(17.086f, 17f, 16.89f, 16.468f, 15.703f, 15.281f)
                curveTo(14.664f, 14.28f, 14.219f, 14.15f, 13.96f, 14.15f)
                curveToRelative(-0.353f, 0f, -0.458f, 0.1f, -0.458f, 0.6f)
                verticalLineToRelative(1.569f)
                curveTo(13.502f, 16.749f, 13.365f, 17f, 12.252f, 17f)
                curveTo(10.398f, 17f, 8.36f, 15.874f, 6.913f, 13.798f)
                curveTo(4.743f, 10.757f, 4.15f, 8.458f, 4.15f, 7.995f)
                curveTo(4.15f, 7.735f, 4.25f, 7.5f, 4.75f, 7.5f)
                horizontalLineToRelative(1.751f)
                curveToRelative(0.447f, 0f, 0.615f, 0.196f, 0.783f, 0.68f)
                curveToRelative(0.856f, 2.493f, 2.3f, 4.672f, 2.893f, 4.672f)
                curveToRelative(0.222f, 0f, 0.324f, -0.103f, 0.324f, -0.667f)
                verticalLineTo(9.608f)
                curveTo(10.436f, 8.422f, 9.805f, 8.324f, 9.805f, 7.902f)
                curveTo(9.805f, 7.707f, 9.972f, 7.5f, 10.25f, 7.5f)
                horizontalLineToRelative(2.751f)
                curveToRelative(0.371f, 0f, 0.5f, 0.198f, 0.5f, 0.643f)
                verticalLineToRelative(3.467f)
                curveToRelative(0f, 0.37f, 0.161f, 0.5f, 0.272f, 0.5f)
                curveToRelative(0.223f, 0f, 0.408f, -0.13f, 0.816f, -0.538f)
                curveToRelative(1.261f, -1.409f, 2.151f, -3.578f, 2.151f, -3.578f)
                curveToRelative(0.112f, -0.26f, 0.316f, -0.495f, 0.762f, -0.495f)
                horizontalLineToRelative(1.75f)
                curveToRelative(0.529f, 0f, 0.641f, 0.272f, 0.529f, 0.643f)
                curveToRelative(-0.223f, 1.02f, -2.355f, 4.023f, -2.355f, 4.023f)
                curveToRelative(-0.186f, 0.297f, -0.26f, 0.445f, 0f, 0.779f)
                curveToRelative(0.186f, 0.26f, 0.797f, 0.779f, 1.205f, 1.261f)
                curveToRelative(0.752f, 0.846f, 1.319f, 1.559f, 1.477f, 2.051f)
                curveTo(20.254f, 16.75f, 20.003f, 17f, 19.503f, 17f)
                close()
            }
        }.build()

        return _Vk!!
    }

@Suppress("ObjectPropertyName")
private var _Vk: ImageVector? = null
