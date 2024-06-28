package ru.bgitu.feature.schedule_widget.model

import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import ru.bgitu.feature.schedule_widget.R

enum class WidgetTheme(
    @StyleRes val themeResId: Int,
    @StringRes val nameResId: Int
) {
    AUTO(
        themeResId = R.style.ThemeWidgetSystem,
        nameResId = R.string.theme_auto
    ),
    DARK(
        themeResId = R.style.ThemeWidgetDark,
        nameResId = R.string.theme_dark
    ),
    LIGHT(
        themeResId = R.style.ThemeWidgetLight,
        nameResId = R.string.theme_light
    ),
    DYNAMIC(
        themeResId = R.style.ThemeWidgetSystem,
        nameResId = R.string.theme_dynamic
    )
}