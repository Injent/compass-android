package ru.bgitu.feature.schedule_widget.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import ru.bgitu.feature.schedule_widget.R

enum class WidgetTextColor(
    @ColorRes val colorResId: Int,
    @StringRes val nameResId: Int
) {
    FROM_THEME(
        colorResId = R.color.white,
        nameResId = R.string.text_color_from_theme
    ),
    BLACK(
        colorResId = R.color.black,
        nameResId = R.string.text_color_black
    ),
    WHITE(
        colorResId = R.color.white,
        nameResId = R.string.text_color_white
    )
}