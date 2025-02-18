package ru.bgitu.feature.schedule_widget.model

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.glance.unit.ColorProvider
import ru.bgitu.feature.schedule_widget.R

enum class WidgetThemeMode {
    AUTO,
    LIGHT,
    DARK
}

class WidgetColorScheme private constructor(
    val brand: ColorProvider,
    val onBrand: ColorProvider,
    val background1: ColorProvider,
    val background2: ColorProvider,
    val foreground1: ColorProvider,
    val foreground2: ColorProvider,
    val widgetBackground: ColorProvider,
) {
    companion object {
        fun createFrom(
            context: Context,
            mode: WidgetThemeMode,
            @FloatRange(0.0, 1.0) opacity: Float,
        ): WidgetColorScheme {
            fun getColor(@ColorRes id: Int, applyOpacity: Boolean = true): ColorProvider {
                return ColorProvider(
                    Color(
                        ContextCompat.getColor(context, id)
                    ).let {
                        if (applyOpacity) {
                            it.copy(alpha = opacity)
                        } else it
                    }
                )
            }

            return when (mode) {
                WidgetThemeMode.AUTO -> WidgetColorScheme(
                    brand = getColor(R.color.colorBrand, false),
                    onBrand = getColor(R.color.colorOnBrand, false),
                    background1 = getColor(R.color.colorBackground1),
                    background2 = getColor(R.color.colorBackground2),
                    foreground1 = getColor(R.color.colorForeground1, false),
                    foreground2 = getColor(R.color.colorForeground2, false),
                    widgetBackground = getColor(R.color.colorWidgetBackground),
                )
                WidgetThemeMode.LIGHT -> WidgetColorScheme(
                    brand = getColor(R.color.static_light_colorBrand, false),
                    onBrand = getColor(R.color.static_light_colorOnBrand, false),
                    background1 = getColor(R.color.static_light_colorBackground1),
                    background2 = getColor(R.color.static_light_colorBackground2),
                    foreground1 = getColor(R.color.static_light_colorForeground1, false),
                    foreground2 = getColor(R.color.static_light_colorForeground2, false),
                    widgetBackground = getColor(R.color.static_light_colorWidgetBackground),
                )
                WidgetThemeMode.DARK -> WidgetColorScheme(
                    brand = getColor(R.color.static_dark_colorBrand, false),
                    onBrand = getColor(R.color.static_dark_colorOnBrand, false),
                    background1 = getColor(R.color.static_dark_colorBackground1),
                    background2 = getColor(R.color.static_dark_colorBackground2),
                    foreground1 = getColor(R.color.static_dark_colorForeground1, false),
                    foreground2 = getColor(R.color.static_dark_colorForeground2, false),
                    widgetBackground = getColor(R.color.static_dark_colorWidgetBackground),
                )
            }
        }
    }
}