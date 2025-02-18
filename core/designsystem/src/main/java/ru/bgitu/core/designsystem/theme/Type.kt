package ru.bgitu.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import ru.bgitu.core.designsystem.R

internal val AlshaussFont = FontFamily(
    Font(R.font.alshauss_black, FontWeight.Black),
    Font(R.font.alshauss_bold, FontWeight.Bold),
    Font(R.font.alshauss_medium, FontWeight.Medium),
    Font(R.font.alshauss_regular, FontWeight.Normal),
    Font(R.font.alshauss_light, FontWeight.Light),
    Font(R.font.alshauss_thin, FontWeight.Thin),
)

private val baselineShift = BaselineShift(-0f)
private val platformStyle = PlatformTextStyle(includeFontPadding = true)
private val lineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.Both
)

@Immutable
data class AppTypography(
    val xLargeTitle: TextStyle,
    val largeTitle: TextStyle,
    val title1: TextStyle,
    val title2: TextStyle,
    val title3: TextStyle,
    val headline1: TextStyle,
    val headline2: TextStyle,
    val body: TextStyle,
    val bodyButton: TextStyle,
    val subheadline: TextStyle,
    val subheadlineButton: TextStyle,
    val callout: TextStyle,
    val calloutButton: TextStyle,
    val footnote: TextStyle,
    val footstrike: TextStyle,
    val caption1: TextStyle,
    val caption2: TextStyle
)

val Typography = AppTypography(
    xLargeTitle = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Bold,
        fontSize = 44.sp,
        lineHeight = 48.sp,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    largeTitle = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.0025.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    title1 = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.0025.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    title2 = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.0025.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    title3 = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.0015.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    headline1 = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.0015.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    headline2 = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.0015.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    body = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.0015.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    bodyButton = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.0015.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    subheadline = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.001.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    subheadlineButton = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.001.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    callout = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.0025.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    calloutButton = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.0025.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    footnote = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.004.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    footstrike = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.004.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    caption1 = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.004.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
    caption2 = TextStyle(
        fontFamily = AlshaussFont,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.004.em,
        baselineShift = baselineShift,
        platformStyle = platformStyle,
        lineHeightStyle = lineHeightStyle
    ),
)