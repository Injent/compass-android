package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Notification: ImageVector
    get() {
        if (_Notification != null) {
            return _Notification!!
        }
        _Notification = ImageVector.Builder(
            name = "Notification",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF185AC5)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(17.496f, 12.594f)
                lineTo(18.52f, 13.741f)
                curveTo(19.043f, 14.357f, 19.134f, 15.164f, 18.816f, 15.865f)
                curveTo(18.452f, 16.566f, 17.724f, 17.012f, 16.882f, 17.012f)
                horizontalLineTo(13.652f)
                curveTo(13.389f, 18.702f, 11.848f, 20f, 9.985f, 20f)
                curveTo(8.123f, 20f, 6.581f, 18.702f, 6.319f, 17.012f)
                horizontalLineTo(3.145f)
                curveTo(2.304f, 17.012f, 1.576f, 16.566f, 1.212f, 15.865f)
                curveTo(0.848f, 15.164f, 0.962f, 14.336f, 1.485f, 13.741f)
                lineTo(2.486f, 12.594f)
                curveTo(3.122f, 11.872f, 3.464f, 10.959f, 3.464f, 10.025f)
                verticalLineTo(7.88f)
                curveTo(3.464f, 4.779f, 5.965f, 2.209f, 9.172f, 1.827f)
                verticalLineTo(0.765f)
                curveTo(9.172f, 0.34f, 9.536f, 0f, 9.991f, 0f)
                curveTo(10.446f, 0f, 10.81f, 0.34f, 10.81f, 0.765f)
                verticalLineTo(1.827f)
                curveTo(14.017f, 2.209f, 16.518f, 4.779f, 16.518f, 7.88f)
                verticalLineTo(10.025f)
                curveTo(16.518f, 10.959f, 16.86f, 11.872f, 17.496f, 12.594f)
                close()
                moveTo(14.93f, 10.025f)
                curveTo(14.93f, 11.302f, 15.396f, 12.557f, 16.276f, 13.554f)
                lineTo(17.289f, 14.69f)
                curveTo(17.413f, 14.843f, 17.448f, 15.038f, 17.371f, 15.238f)
                curveTo(17.263f, 15.419f, 17.087f, 15.512f, 16.882f, 15.512f)
                horizontalLineTo(3.145f)
                curveTo(2.928f, 15.512f, 2.743f, 15.408f, 2.638f, 15.205f)
                curveTo(2.554f, 15.044f, 2.576f, 14.848f, 2.706f, 14.7f)
                lineTo(3.709f, 13.551f)
                curveTo(4.587f, 12.554f, 5.052f, 11.3f, 5.052f, 10.025f)
                verticalLineTo(7.88f)
                curveTo(5.052f, 5.56f, 6.931f, 3.606f, 9.371f, 3.315f)
                curveTo(9.597f, 3.288f, 9.747f, 3.28f, 9.991f, 3.28f)
                curveTo(10.234f, 3.28f, 10.385f, 3.288f, 10.611f, 3.315f)
                curveTo(13.051f, 3.606f, 14.93f, 5.56f, 14.93f, 7.88f)
                verticalLineTo(10.025f)
                close()
                moveTo(9.985f, 18.5f)
                curveTo(9.003f, 18.5f, 8.177f, 17.869f, 7.938f, 17.012f)
                horizontalLineTo(12.033f)
                curveTo(11.793f, 17.869f, 10.967f, 18.5f, 9.985f, 18.5f)
                close()
            }
        }.build()

        return _Notification!!
    }

@Suppress("ObjectPropertyName")
private var _Notification: ImageVector? = null
