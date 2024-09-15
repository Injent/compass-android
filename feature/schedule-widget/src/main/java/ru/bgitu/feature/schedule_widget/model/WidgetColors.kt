package ru.bgitu.feature.schedule_widget.model

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.ui.graphics.Color
import androidx.glance.color.DynamicThemeColorProviders
import ru.bgitu.feature.schedule_widget.R

data class WidgetColors(
    val background: Color,
    val onBackground: Color,
    val container: Color,
    val onContainer: Color,
)

fun provideWidgetColors(context: Context, options: WidgetOptions): WidgetColors {
    fun colorOrDefault(widgetTextColor: WidgetTextColor, default: Color): Color {
        return if (widgetTextColor == WidgetTextColor.FROM_THEME) {
            default
        } else Color(context.getColor(widgetTextColor.colorResId))
    }

    if (options.widgetTheme == WidgetTheme.DYNAMIC) {
        return with(DynamicThemeColorProviders) {
            WidgetColors(
                background = background.getColor(context),
                onBackground = colorOrDefault(options.elementsColor, onBackground.getColor(context)),
                container = secondaryContainer.getColor(context),
                onContainer = colorOrDefault(options.textColor, onSecondaryContainer.getColor(context))
            )
        }
    }

    val androidTheme = ContextThemeWrapper(context, options.widgetTheme.themeResId).theme

    fun color(@AttrRes attr: Int): Color {
        @ColorInt val argb = TypedValue().let {
            androidTheme.resolveAttribute(attr, it, true)
            it.data
        }
        return Color(argb)
    }

    return WidgetColors(
        background = color(R.attr.colorBackground),
        onBackground = colorOrDefault(options.elementsColor, color(R.attr.colorOnBackground)),
        container = color(R.attr.colorContainer),
        onContainer = colorOrDefault(options.textColor, color(R.attr.colorOnContainer))
    )
}