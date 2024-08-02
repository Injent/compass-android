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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowCompat

@Immutable
data class AppDimen(
    val default: Dp,
    val screenPadding: Dp,
    val commonScreenPadding: Dp,
    val arrangementSpace: Dp,
    val extraSmallSpace: Dp,
    val smallSpace: Dp,
    val mediumSpace: Dp,
    val largeSpace: Dp,
    val iconSize: Dp,
    val smallIconSize: Dp,
    val cardElevation: Dp,
    val contentPadding: Dp
)

@Immutable
data class AppShapes(
    val default: RoundedCornerShape,
    val large: RoundedCornerShape,
    val small: RoundedCornerShape,
    val defaultTopCarved: RoundedCornerShape,
    val defaultBottomCarved: RoundedCornerShape
)

@Stable
data class ColorScheme(
    val whiteBackground: Color,
    val whiteCard: Color,
    val whiteText: Color,
    val whiteOpacity: Color,
    val ripple: Color,
    val blackText: Color,
    val graySnackbar: Color,
    val grayDark: Color,
    val grayDisabled: Color,
    val gray1: Color,
    val gray2: Color,
    val grayOverlay: Color,
    val grayLight: Color,
    val grayFooter: Color,
    val grayBody: Color,
    val blueCobalt: Color,
    val blue: Color,
    val blue1: Color = blue,
    val blue2: Color = blue,
    val blueChateau: Color,
    val blueWave: Color,
    val lightBlue1: Color,
    val lightBlue2: Color,
    val lightBlue3: Color,
    val blueLavender: Color,
    val yellow: Color,
    val red: Color,
    val destructive: Color,
    val redPalePink: Color
)

object AppTheme {
    val typography: AppTypography
        @Composable get() = LocalTypography.current
    val shapes: AppShapes
        @Composable get() = LocalShapes.current

    val colors: ColorScheme
        @Composable get() = LocalColorScheme.current

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

val LocalColorScheme = staticCompositionLocalOf<ColorScheme> {
    error("Color scheme not provided")
}

val LocalAppRipple = staticCompositionLocalOf<AppRipple> {
    error("AppRipple not provided")
}

@Composable
fun CompassTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColorAllowed: Boolean = false,
    isTranslucent: Boolean = false,
    content: @Composable () -> Unit
) {
    val view = LocalView.current

    val dynamicColorsEnabled = isDynamicColorsEnabled(dynamicColorAllowed = dynamicColorAllowed)
    val colorScheme = createColorScheme(
        darkTheme = darkTheme,
        dynamicColorsEnabled = dynamicColorsEnabled
    )
    val newColorScheme = if (darkTheme) NewDarkColorScheme else NewLightColorScheme
    val ripple = if (darkTheme) AppDarkRipple else AppLightRipple

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
            if (!isTranslucent) {
                window.decorView.setBackgroundColor(newColorScheme.background2.toArgb())
            }
        }
    }

    CompositionLocalProvider(
        LocalTypography provides Typography,
        LocalShapes provides Shapes,
        LocalColorScheme provides colorScheme,
        LocalAppRipple provides ripple,
        LocalNewSpacing provides Spacing,
        LocalStrokeWidth provides StrokeWidth,
        LocalNewColorScheme provides newColorScheme
    ) {
        MaterialTheme(
            content = content,
            colorScheme = MaterialTheme.colorScheme.copy(
                background = newColorScheme.background2,
                surface = newColorScheme.background2,
                onSurface = AppTheme.colorScheme.foreground3
            )
        )
    }
}

@Composable
private fun createColorScheme(
    darkTheme: Boolean,
    dynamicColorsEnabled: Boolean
): ColorScheme {
    @Composable
    fun provideColor(@ColorRes id: Int): Color {
        val context = LocalContext.current
        return Color(context.resources.getColor(id, context.theme))
    }

    return if (darkTheme) {
        if (dynamicColorsEnabled && SDK_INT >= 31) {
            DarkColorScheme.copy(
                blue = provideColor(android.R.color.system_accent1_400),
                blue1 = provideColor(android.R.color.system_accent1_300),
                blue2 = provideColor(android.R.color.system_accent1_500),
                gray1 = provideColor(android.R.color.system_neutral1_600),
                gray2 = provideColor(android.R.color.system_neutral2_500),
                //lightBlue1 = provideColor(android.R.color.system_accent2_800),
                //lightBlue2 = provideColor(android.R.color.system_neutral1_900),
                //blueWave = provideColor(android.R.color.system_accent2_800),
                //whiteCard = provideColor(android.R.color.system_neutral1_900),
                blueCobalt = provideColor(android.R.color.system_accent1_500)
            )
        } else DarkColorScheme
    } else {
        if (dynamicColorsEnabled && SDK_INT >= 31) {
            LightColorScheme.copy(
                blue = provideColor(android.R.color.system_accent1_300),
                blue1 = provideColor(android.R.color.system_accent1_300),
                blue2 = provideColor(android.R.color.system_accent1_200),
                gray1 = provideColor(android.R.color.system_neutral1_300),
                //gray2 = provideColor(android.R.color.system_neutral1_200),
                //whiteBackground = provideColor(android.R.color.system_accent1_10),
                //lightBlue2 = provideColor(android.R.color.system_accent1_50),
                //lightBlue1 = provideColor(android.R.color.system_accent1_50),
                //blueWave = provideColor(android.R.color.system_accent2_100),
                //whiteCard = provideColor(android.R.color.system_accent2_10),
                blackText = provideColor(android.R.color.system_accent1_900),
                blueCobalt = provideColor(android.R.color.system_accent1_400)
            )
        } else LightColorScheme
    }
}

@Composable
private fun isDynamicColorsEnabled(dynamicColorAllowed: Boolean): Boolean {
    val context = LocalContext.current
    return dynamicColorAllowed && SDK_INT >= 31 && run {
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