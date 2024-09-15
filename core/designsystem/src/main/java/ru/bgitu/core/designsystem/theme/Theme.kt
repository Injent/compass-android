package ru.bgitu.core.designsystem.theme

import android.app.Activity
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.ColorRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Immutable
data class AppShapes(
    val default: RoundedCornerShape,
    val large: RoundedCornerShape,
    val small: RoundedCornerShape,
    val extraSmall: RoundedCornerShape,
    val defaultTopCarved: RoundedCornerShape,
    val defaultBottomCarved: RoundedCornerShape
)

object AppTheme {
    val typography: AppTypography
        @Composable get() = LocalTypography.current
    val shapes: AppShapes
        @Composable get() = LocalShapes.current

    val colorScheme: NewColorScheme
        @Composable get() = LocalNewColorScheme.current

    val spacing: NewSpacing
        @Composable get() = LocalNewSpacing.current

    val strokeWidth: AppStrokeWidth
        @Composable get() = LocalStrokeWidth.current

    val isDarkTheme: Boolean
        @Composable get()= LocalNewColorScheme.current.isDarkTheme
}

val LocalTypography = staticCompositionLocalOf { Typography }

val LocalShapes = staticCompositionLocalOf { Shapes }

val LocalNewColorScheme = staticCompositionLocalOf<NewColorScheme> {
    error("ColorScheme not provided")
}

val LocalStrokeWidth = staticCompositionLocalOf { StrokeWidth }

val LocalNewSpacing = staticCompositionLocalOf { Spacing }

val LocalAppRipple = staticCompositionLocalOf<AppRipple> {
    error("AppRipple not provided")
}

@Composable
fun CompassTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColorAvailable: Boolean = false,
    isTranslucent: Boolean = false,
    content: @Composable () -> Unit
) {
    val view = LocalView.current

    val colorScheme = createColorScheme(
        darkTheme = darkTheme,
        dynamicColorsEnabled = dynamicColorAvailable && isDynamicColorsEnabled()
    )
    val ripple = if (darkTheme) AppDarkRipple else AppLightRipple

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
            if (!isTranslucent) {
                window.decorView.setBackgroundColor(colorScheme.background2.toArgb())
            }
        }
    }

    CompositionLocalProvider(
        LocalTypography provides Typography,
        LocalShapes provides Shapes,
        LocalAppRipple provides ripple,
        LocalNewSpacing provides Spacing,
        LocalStrokeWidth provides StrokeWidth,
        LocalNewColorScheme provides colorScheme
    ) {
        MaterialTheme(
            content = content,
            colorScheme = MaterialTheme.colorScheme.copy(
                background = colorScheme.background2,
                surface = colorScheme.background2,
                onSurface = AppTheme.colorScheme.foreground3
            )
        )
    }
}

@Composable
private fun createColorScheme(
    darkTheme: Boolean,
    dynamicColorsEnabled: Boolean
): NewColorScheme {
    val context = LocalContext.current

    @Composable
    fun provideColor(@ColorRes id: Int): Color {
        return Color(context.resources.getColor(id, context.theme))
    }

    return if (darkTheme) {
        if (dynamicColorsEnabled && SDK_INT >= 31) {
            NewDarkColorScheme.copy(
                backgroundBrand = provideColor(android.R.color.system_accent1_200),
                foregroundOnBrand = provideColor(android.R.color.system_accent1_800),
                foreground = provideColor(android.R.color.system_accent1_200),
                foregroundAccent = provideColor(android.R.color.system_accent2_200)
            )
        } else NewDarkColorScheme
    } else {
        if (dynamicColorsEnabled && SDK_INT >= 31) {
            NewLightColorScheme.copy(
                backgroundBrand = provideColor(android.R.color.system_accent1_600),
                foregroundOnBrand = provideColor(android.R.color.system_accent1_0),
                foreground = provideColor(android.R.color.system_accent1_600),
                foregroundAccent = provideColor(android.R.color.system_accent2_600),
            )
        } else NewLightColorScheme
    }
}

@Composable
private fun isDynamicColorsEnabled(): Boolean {
    val context = LocalContext.current

    val isDefaultColorsReceived = SDK_INT >= 31 && run {
        val defaultDynamicDarkColor = Color(0xFF219BCC)
        val userDynamicDarkColor = Color(
            context.resources.getColor(android.R.color.system_accent1_400, context.theme)
        )

        val defaultDynamicLightColor = Color(0xFF4BB6E8)
        val userDynamicLightColor = Color(
            context.resources.getColor(android.R.color.system_accent1_300, context.theme)
        )

        userDynamicDarkColor != defaultDynamicDarkColor
                && userDynamicLightColor != defaultDynamicLightColor
    }
    return isDefaultColorsReceived
}

@Composable
fun Color.applyState(isPressed: Boolean, enabled: Boolean): Color {
    return when (this) {
        AppTheme.colorScheme.backgroundBrand -> {
            when {
                !enabled -> AppTheme.colorScheme.backgroundDisabled
                isPressed -> AppTheme.colorScheme.backgroundPressed
                else -> this
            }
        }
        AppTheme.colorScheme.backgroundTouchable -> {
            when {
                !enabled -> AppTheme.colorScheme.backgroundDisabled.copy(.5f)
                isPressed -> AppTheme.colorScheme.backgroundTouchablePressed
                else -> this
            }
        }
        AppTheme.colorScheme.background1 -> {
            when {
                !enabled -> AppTheme.colorScheme.backgroundDisabled
                isPressed -> AppTheme.colorScheme.background1.copy(.75f)
                else -> this
            }
        }
        AppTheme.colorScheme.foreground -> {
            when {
                !enabled -> AppTheme.colorScheme.foregroundDisabled
                isPressed -> AppTheme.colorScheme.foregroundPressed
                else -> this
            }
        }
        AppTheme.colorScheme.foregroundOnBrand -> {
            when {
                !enabled -> AppTheme.colorScheme.foregroundOnBrand.copy(.5f)
                isPressed -> AppTheme.colorScheme.foregroundOnBrand.copy(.95f)
                else -> this
            }
        }
        else -> this
    }
}