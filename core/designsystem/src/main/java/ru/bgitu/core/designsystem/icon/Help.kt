package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Help: ImageVector
    get() {
        if (_Help != null) {
            return _Help!!
        }
        _Help = ImageVector.Builder(
            name = "Help",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(10.234f, 12.933f)
                curveTo(10.4f, 12.933f, 10.533f, 12.799f, 10.544f, 12.633f)
                curveTo(10.565f, 12.332f, 10.616f, 12.046f, 10.698f, 11.776f)
                curveTo(10.807f, 11.419f, 11.02f, 11.098f, 11.336f, 10.813f)
                curveTo(11.704f, 10.478f, 12.052f, 10.126f, 12.379f, 9.757f)
                curveTo(12.707f, 9.388f, 12.974f, 8.994f, 13.181f, 8.575f)
                curveTo(13.394f, 8.151f, 13.5f, 7.687f, 13.5f, 7.184f)
                curveTo(13.5f, 6.531f, 13.365f, 5.966f, 13.095f, 5.492f)
                curveTo(12.825f, 5.011f, 12.431f, 4.642f, 11.914f, 4.385f)
                curveTo(11.402f, 4.128f, 10.782f, 4f, 10.052f, 4f)
                curveTo(9.391f, 4f, 8.793f, 4.12f, 8.259f, 4.36f)
                curveTo(7.724f, 4.601f, 7.299f, 4.947f, 6.983f, 5.399f)
                curveTo(6.724f, 5.777f, 6.569f, 6.218f, 6.518f, 6.72f)
                curveTo(6.501f, 6.887f, 6.638f, 7.025f, 6.806f, 7.025f)
                horizontalLineTo(7.898f)
                curveTo(8.007f, 7.025f, 8.095f, 6.937f, 8.106f, 6.828f)
                curveTo(8.136f, 6.527f, 8.227f, 6.277f, 8.379f, 6.078f)
                curveTo(8.563f, 5.832f, 8.805f, 5.651f, 9.103f, 5.534f)
                curveTo(9.402f, 5.416f, 9.718f, 5.358f, 10.052f, 5.358f)
                curveTo(10.454f, 5.358f, 10.79f, 5.433f, 11.06f, 5.584f)
                curveTo(11.336f, 5.735f, 11.543f, 5.95f, 11.681f, 6.229f)
                curveTo(11.825f, 6.508f, 11.897f, 6.841f, 11.897f, 7.226f)
                curveTo(11.897f, 7.606f, 11.828f, 7.933f, 11.69f, 8.207f)
                curveTo(11.552f, 8.475f, 11.371f, 8.726f, 11.147f, 8.961f)
                curveTo(10.928f, 9.19f, 10.687f, 9.436f, 10.422f, 9.698f)
                curveTo(10.06f, 10.061f, 9.77f, 10.385f, 9.552f, 10.67f)
                curveTo(9.333f, 10.955f, 9.175f, 11.271f, 9.078f, 11.617f)
                curveTo(9.004f, 11.892f, 8.958f, 12.229f, 8.939f, 12.63f)
                curveTo(8.932f, 12.797f, 9.067f, 12.933f, 9.234f, 12.933f)
                horizontalLineTo(10.234f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(9.103f, 14.483f)
                curveTo(8.943f, 14.656f, 8.862f, 14.869f, 8.862f, 15.12f)
                curveTo(8.862f, 15.36f, 8.943f, 15.567f, 9.103f, 15.74f)
                curveTo(9.264f, 15.913f, 9.497f, 16f, 9.802f, 16f)
                curveTo(10.112f, 16f, 10.348f, 15.913f, 10.509f, 15.74f)
                curveTo(10.67f, 15.567f, 10.75f, 15.36f, 10.75f, 15.12f)
                curveTo(10.75f, 14.869f, 10.67f, 14.656f, 10.509f, 14.483f)
                curveTo(10.348f, 14.31f, 10.112f, 14.224f, 9.802f, 14.224f)
                curveTo(9.497f, 14.224f, 9.264f, 14.31f, 9.103f, 14.483f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(20f, 10f)
                curveTo(20f, 15.523f, 15.523f, 20f, 10f, 20f)
                curveTo(4.477f, 20f, 0f, 15.523f, 0f, 10f)
                curveTo(0f, 4.477f, 4.477f, 0f, 10f, 0f)
                curveTo(15.523f, 0f, 20f, 4.477f, 20f, 10f)
                close()
                moveTo(18.5f, 10f)
                curveTo(18.5f, 14.694f, 14.694f, 18.5f, 10f, 18.5f)
                curveTo(5.306f, 18.5f, 1.5f, 14.694f, 1.5f, 10f)
                curveTo(1.5f, 5.306f, 5.306f, 1.5f, 10f, 1.5f)
                curveTo(14.694f, 1.5f, 18.5f, 5.306f, 18.5f, 10f)
                close()
            }
        }.build()

        return _Help!!
    }

@Suppress("ObjectPropertyName")
private var _Help: ImageVector? = null
