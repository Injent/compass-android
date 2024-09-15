package ru.bgitu.core.designsystem.util.shadow

import android.os.Parcelable
import androidx.annotation.FloatRange

/**
 * Описание точки градиента
 *
 * @param point - удаление от начала градиента.
 * 0.0 - начало градинета. 1.0 - конец градиента
 *
 * @param colorMultiplier - изменение цвета в точке.
 * 0.0 - цвет тени, 1.0 - прозрачный цвет
 */
data class GradientPointAndColorMultiplier(
    @FloatRange(from = 0.0, to = 1.0) val point: Float,
    @FloatRange(from = 0.0, to = 1.0) val colorMultiplier: Float
)