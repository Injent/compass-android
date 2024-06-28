package ru.bgitu.core.designsystem.theme

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

data class AppRipple(
    val background1: Color,
    val foreground1: Color
)

val AppLightRipple = AppRipple(
    background1 = Color(0x141B1B1B),
    foreground1 = Color(0xFF242424)
)

val AppDarkRipple = AppRipple(
    background1 = Color(0x14F7F6F6),
    foreground1 = Color.White
)

object DefaultRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color {
        return LocalAppRipple.current.background1
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        val alpha = defaultColor().alpha
        return RippleAlpha(
            draggedAlpha = alpha,
            focusedAlpha = alpha,
            hoveredAlpha = alpha,
            pressedAlpha = alpha
        )
    }
}

object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f,0.0f,0.0f,0.0f)
}

@Composable
fun AppRippleTheme(
    rippleTheme: RippleTheme = DefaultRippleTheme,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalRippleTheme provides rippleTheme, content = content)
}