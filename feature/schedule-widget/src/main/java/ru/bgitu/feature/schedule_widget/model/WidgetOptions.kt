package ru.bgitu.feature.schedule_widget.model

import kotlinx.serialization.Serializable
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetOptionsPb

@Serializable
data class WidgetOptions(
    val opacity: Float = 1f,
    val widgetTheme: WidgetTheme = WidgetTheme.AUTO,
    val textColor: WidgetTextColor = WidgetTextColor.FROM_THEME,
    val elementsColor: WidgetTextColor = WidgetTextColor.FROM_THEME
)

fun WidgetOptions.toProtoModel(): WidgetOptionsPb {
    return WidgetOptionsPb.newBuilder()
        .setTheme(widgetTheme.name)
        .setOpacity(opacity)
        .setTextColor(textColor.name)
        .setElementsColor(elementsColor.name)
        .build()
}