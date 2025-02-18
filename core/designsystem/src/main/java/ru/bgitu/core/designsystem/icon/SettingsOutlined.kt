package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.SettingsOutlined: ImageVector
    get() {
        if (_SettingsOutlined != null) {
            return _SettingsOutlined!!
        }
        _SettingsOutlined = ImageVector.Builder(
            name = "SettingsOutlined",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF185AC5)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(10f, 1.667f)
                curveTo(9.785f, 1.667f, 9.572f, 1.674f, 9.361f, 1.689f)
                curveTo(9.106f, 1.708f, 8.836f, 1.923f, 8.78f, 2.298f)
                lineTo(8.699f, 2.842f)
                curveTo(8.559f, 3.778f, 7.898f, 4.474f, 7.163f, 4.858f)
                curveTo(7.052f, 4.915f, 6.944f, 4.977f, 6.838f, 5.04f)
                curveTo(6.127f, 5.469f, 5.176f, 5.678f, 4.271f, 5.327f)
                lineTo(3.745f, 5.123f)
                curveTo(3.383f, 4.983f, 3.056f, 5.101f, 2.911f, 5.307f)
                curveTo(2.672f, 5.648f, 2.457f, 6.007f, 2.271f, 6.383f)
                curveTo(2.16f, 6.606f, 2.217f, 6.939f, 2.523f, 7.173f)
                lineTo(2.971f, 7.518f)
                curveTo(3.735f, 8.103f, 4.025f, 9.005f, 3.999f, 9.816f)
                lineTo(3.996f, 10f)
                lineTo(3.999f, 10.184f)
                curveTo(4.025f, 10.995f, 3.735f, 11.898f, 2.971f, 12.483f)
                lineTo(2.523f, 12.827f)
                curveTo(2.217f, 13.061f, 2.16f, 13.394f, 2.271f, 13.618f)
                curveTo(2.457f, 13.993f, 2.672f, 14.352f, 2.911f, 14.693f)
                curveTo(3.056f, 14.899f, 3.383f, 15.017f, 3.745f, 14.877f)
                lineTo(4.271f, 14.673f)
                curveTo(5.176f, 14.323f, 6.127f, 14.532f, 6.838f, 14.96f)
                curveTo(6.944f, 15.023f, 7.052f, 15.085f, 7.163f, 15.142f)
                curveTo(7.898f, 15.526f, 8.559f, 16.222f, 8.699f, 17.158f)
                lineTo(8.78f, 17.702f)
                curveTo(8.836f, 18.077f, 9.106f, 18.292f, 9.361f, 18.311f)
                curveTo(9.572f, 18.326f, 9.785f, 18.333f, 10f, 18.333f)
                curveTo(10.214f, 18.333f, 10.428f, 18.326f, 10.639f, 18.311f)
                curveTo(10.894f, 18.292f, 11.164f, 18.077f, 11.219f, 17.702f)
                lineTo(11.301f, 17.158f)
                curveTo(11.441f, 16.222f, 12.102f, 15.526f, 12.837f, 15.142f)
                curveTo(12.948f, 15.085f, 13.056f, 15.023f, 13.163f, 14.96f)
                curveTo(13.872f, 14.532f, 14.823f, 14.323f, 15.728f, 14.673f)
                lineTo(16.255f, 14.877f)
                curveTo(16.617f, 15.017f, 16.944f, 14.899f, 17.089f, 14.693f)
                curveTo(17.328f, 14.352f, 17.542f, 13.993f, 17.728f, 13.618f)
                curveTo(17.84f, 13.394f, 17.782f, 13.061f, 17.477f, 12.827f)
                lineTo(17.028f, 12.483f)
                curveTo(16.265f, 11.898f, 15.974f, 10.995f, 16.001f, 10.184f)
                lineTo(16.004f, 10f)
                lineTo(16.001f, 9.816f)
                curveTo(15.974f, 9.005f, 16.265f, 8.103f, 17.028f, 7.518f)
                lineTo(17.477f, 7.173f)
                curveTo(17.782f, 6.939f, 17.84f, 6.606f, 17.728f, 6.383f)
                curveTo(17.542f, 6.008f, 17.328f, 5.648f, 17.089f, 5.307f)
                curveTo(16.944f, 5.101f, 16.617f, 4.983f, 16.255f, 5.123f)
                lineTo(15.728f, 5.327f)
                curveTo(14.823f, 5.678f, 13.872f, 5.469f, 13.163f, 5.04f)
                curveTo(13.056f, 4.977f, 12.948f, 4.915f, 12.837f, 4.858f)
                curveTo(12.102f, 4.474f, 11.441f, 3.778f, 11.301f, 2.842f)
                lineTo(11.219f, 2.298f)
                curveTo(11.164f, 1.923f, 10.894f, 1.708f, 10.639f, 1.689f)
                curveTo(10.428f, 1.674f, 10.214f, 1.667f, 10f, 1.667f)
                close()
                moveTo(9.236f, 0.027f)
                curveTo(9.488f, 0.009f, 9.743f, 0f, 10f, 0f)
                curveTo(10.257f, 0f, 10.512f, 0.009f, 10.764f, 0.027f)
                curveTo(11.952f, 0.112f, 12.765f, 1.042f, 12.917f, 2.058f)
                lineTo(12.998f, 2.602f)
                curveTo(13.041f, 2.889f, 13.262f, 3.188f, 13.648f, 3.389f)
                curveTo(13.791f, 3.463f, 13.931f, 3.542f, 14.067f, 3.625f)
                curveTo(14.439f, 3.849f, 14.816f, 3.886f, 15.093f, 3.778f)
                lineTo(15.62f, 3.574f)
                curveTo(16.601f, 3.194f, 17.834f, 3.413f, 18.505f, 4.367f)
                curveTo(18.792f, 4.776f, 19.05f, 5.207f, 19.274f, 5.657f)
                curveTo(19.791f, 6.699f, 19.368f, 7.847f, 18.539f, 8.483f)
                lineTo(18.091f, 8.826f)
                curveTo(17.857f, 9.005f, 17.702f, 9.34f, 17.715f, 9.764f)
                curveTo(17.718f, 9.842f, 17.719f, 9.921f, 17.719f, 10f)
                curveTo(17.719f, 10.079f, 17.718f, 10.158f, 17.715f, 10.236f)
                curveTo(17.702f, 10.66f, 17.857f, 10.995f, 18.091f, 11.174f)
                lineTo(18.539f, 11.517f)
                curveTo(19.368f, 12.153f, 19.791f, 13.301f, 19.274f, 14.342f)
                curveTo(19.05f, 14.792f, 18.792f, 15.224f, 18.505f, 15.633f)
                curveTo(17.834f, 16.587f, 16.601f, 16.806f, 15.62f, 16.426f)
                lineTo(15.093f, 16.222f)
                curveTo(14.816f, 16.114f, 14.439f, 16.151f, 14.067f, 16.375f)
                curveTo(13.931f, 16.458f, 13.791f, 16.537f, 13.648f, 16.611f)
                curveTo(13.262f, 16.813f, 13.041f, 17.111f, 12.998f, 17.397f)
                lineTo(12.917f, 17.942f)
                curveTo(12.765f, 18.958f, 11.952f, 19.888f, 10.764f, 19.972f)
                curveTo(10.512f, 19.991f, 10.257f, 20f, 10f, 20f)
                curveTo(9.743f, 20f, 9.488f, 19.991f, 9.236f, 19.972f)
                curveTo(8.049f, 19.888f, 7.235f, 18.958f, 7.083f, 17.942f)
                lineTo(7.001f, 17.397f)
                curveTo(6.959f, 17.111f, 6.737f, 16.813f, 6.351f, 16.611f)
                curveTo(6.209f, 16.537f, 6.069f, 16.458f, 5.933f, 16.375f)
                curveTo(5.561f, 16.151f, 5.184f, 16.114f, 4.907f, 16.222f)
                lineTo(4.379f, 16.426f)
                curveTo(3.399f, 16.806f, 2.166f, 16.587f, 1.495f, 15.633f)
                curveTo(1.208f, 15.224f, 0.95f, 14.792f, 0.727f, 14.342f)
                curveTo(0.209f, 13.301f, 0.631f, 12.153f, 1.461f, 11.517f)
                lineTo(1.909f, 11.174f)
                curveTo(2.143f, 10.995f, 2.298f, 10.66f, 2.284f, 10.236f)
                curveTo(2.282f, 10.158f, 2.281f, 10.079f, 2.281f, 10f)
                curveTo(2.281f, 9.921f, 2.282f, 9.842f, 2.284f, 9.764f)
                curveTo(2.298f, 9.34f, 2.143f, 9.005f, 1.909f, 8.826f)
                lineTo(1.461f, 8.483f)
                curveTo(0.631f, 7.847f, 0.209f, 6.699f, 0.727f, 5.657f)
                curveTo(0.95f, 5.207f, 1.208f, 4.776f, 1.495f, 4.367f)
                curveTo(2.166f, 3.413f, 3.399f, 3.194f, 4.379f, 3.574f)
                lineTo(4.907f, 3.778f)
                curveTo(5.184f, 3.886f, 5.561f, 3.849f, 5.933f, 3.625f)
                curveTo(6.069f, 3.542f, 6.209f, 3.463f, 6.351f, 3.389f)
                curveTo(6.737f, 3.188f, 6.959f, 2.889f, 7.001f, 2.602f)
                lineTo(7.083f, 2.058f)
                curveTo(7.235f, 1.042f, 8.049f, 0.112f, 9.236f, 0.027f)
                close()
                moveTo(10f, 7.5f)
                curveTo(8.579f, 7.5f, 7.427f, 8.619f, 7.427f, 10f)
                curveTo(7.427f, 11.381f, 8.579f, 12.5f, 10f, 12.5f)
                curveTo(11.421f, 12.5f, 12.573f, 11.381f, 12.573f, 10f)
                curveTo(12.573f, 8.619f, 11.421f, 7.5f, 10f, 7.5f)
                close()
                moveTo(5.711f, 10f)
                curveTo(5.711f, 7.699f, 7.631f, 5.833f, 10f, 5.833f)
                curveTo(12.368f, 5.833f, 14.288f, 7.699f, 14.288f, 10f)
                curveTo(14.288f, 12.301f, 12.368f, 14.167f, 10f, 14.167f)
                curveTo(7.631f, 14.167f, 5.711f, 12.301f, 5.711f, 10f)
                close()
            }
        }.build()

        return _SettingsOutlined!!
    }

@Suppress("ObjectPropertyName")
private var _SettingsOutlined: ImageVector? = null
