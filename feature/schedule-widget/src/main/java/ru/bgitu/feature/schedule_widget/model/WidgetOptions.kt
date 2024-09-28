package ru.bgitu.feature.schedule_widget.model

import kotlinx.serialization.Serializable
import ru.bgitu.feature.schedule_widget.WidgetPb.WidgetOptionsPb

@Serializable
data class WidgetOptions(
    val opacity: Float = 1f,
    val themeMode: WidgetThemeMode = WidgetThemeMode.AUTO
)

fun WidgetOptions.toProtoModel(): WidgetOptionsPb {
    return WidgetOptionsPb.newBuilder()
        .setOpacity(opacity)
        .setMode(themeMode.ordinal)
        .build()
}