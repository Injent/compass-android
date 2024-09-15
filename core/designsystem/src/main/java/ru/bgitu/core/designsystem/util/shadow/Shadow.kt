package ru.bgitu.core.designsystem.util.shadow

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.Px
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt

/**
 * Параметры тени
 *
 * @Param dX - смещение по оси X
 * @Param dY - смещение по оси Y
 * @Param radius - радиус размытия тени
 * @Param color - цвет тени
 * @Param colorAlpha - прозрачность цвета тени
 * @Param linearGradientParams - параметры линейного градиента
 */
data class Shadow(
    @Px val dX: Float,
    @Px val dY: Float,
    @Px val radius: Float,
    @ColorInt val color: Int,
    @FloatRange(from = 0.0, to = 1.0) val colorAlpha: Float,
    val linearGradientParams: GradientParams
) {

    val colorWithAlpha: Int by lazy {
        ColorUtils.setAlphaComponent(color, (255f * colorAlpha).roundToInt())
    }

    companion object {
        val NO_SHADOW = Shadow(
            dX = 0f,
            dY = 0f,
            radius = 0f,
            color = 0,
            linearGradientParams = GradientParams.defaultLinearGradient(),
            colorAlpha = 0f
        )
    }
}