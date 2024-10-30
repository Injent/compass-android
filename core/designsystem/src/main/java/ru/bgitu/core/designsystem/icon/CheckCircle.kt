package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.CheckCircle: ImageVector
    get() {
        if (_CheckCircle != null) {
            return _CheckCircle!!
        }
        _CheckCircle = ImageVector.Builder(
            name = "CheckCircle",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveToRelative(12f, 0f)
                curveTo(5.383f, 0f, 0f, 5.383f, 0f, 12f)
                reflectiveCurveToRelative(5.383f, 12f, 12f, 12f)
                reflectiveCurveToRelative(12f, -5.383f, 12f, -12f)
                reflectiveCurveTo(18.617f, 0f, 12f, 0f)
                close()
                moveTo(18.2f, 10.512f)
                lineToRelative(-4.426f, 4.345f)
                curveToRelative(-0.783f, 0.768f, -1.791f, 1.151f, -2.8f, 1.151f)
                curveToRelative(-0.998f, 0f, -1.996f, -0.376f, -2.776f, -1.129f)
                lineToRelative(-1.899f, -1.867f)
                curveToRelative(-0.394f, -0.387f, -0.399f, -1.02f, -0.012f, -1.414f)
                curveToRelative(0.386f, -0.395f, 1.021f, -0.4f, 1.414f, -0.012f)
                lineToRelative(1.893f, 1.861f)
                curveToRelative(0.776f, 0.75f, 2.001f, 0.746f, 2.781f, -0.018f)
                lineToRelative(4.425f, -4.344f)
                curveToRelative(0.393f, -0.388f, 1.024f, -0.381f, 1.414f, 0.013f)
                curveToRelative(0.387f, 0.394f, 0.381f, 1.027f, -0.014f, 1.414f)
                close()
            }
        }.build()

        return _CheckCircle!!
    }

@Suppress("ObjectPropertyName")
private var _CheckCircle: ImageVector? = null
