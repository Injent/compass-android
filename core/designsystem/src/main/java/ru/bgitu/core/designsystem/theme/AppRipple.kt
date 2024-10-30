package ru.bgitu.core.designsystem.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

data class AppRipple(
    val background1: Color,
    val foreground1: Color
)

val AppLightRipple = AppRipple(
    background1 = Color(0x12B4B4B4),
    foreground1 = Color(0xFF242424)
)

val AppDarkRipple = AppRipple(
    background1 = Color(0x14F7F6F6),
    foreground1 = Color.White
)

val DefaultRippleConfig: RippleConfiguration
    @Composable get() {
        val color = LocalAppRipple.current.background1
        return RippleConfiguration(
            color,
            RippleAlpha(
                draggedAlpha = color.alpha,
                focusedAlpha = color.alpha,
                hoveredAlpha = color.alpha,
                pressedAlpha = color.alpha
            )
        )
    }

val NoRippleConfig = null

@Composable
fun AppRippleTheme(
    rippleConfig: RippleConfiguration? = DefaultRippleConfig,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalRippleConfiguration provides rippleConfig, content = content)
}