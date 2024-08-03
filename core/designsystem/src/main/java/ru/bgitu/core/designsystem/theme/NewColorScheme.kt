package ru.bgitu.core.designsystem.theme

import androidx.compose.ui.graphics.Color

data class NewColorScheme(
    // Background Surfaces

    /** Brand color background Eg. button, indicator **/
    val backgroundBrand: Color,

    /** Brand background pressed Eg. button is pressed **/
    val backgroundPressed: Color,

    /** Primary content Eg. Mail, card, list, calendar, and etc. **/
    val background1: Color,

    /** Secondary surface. Primary contents sit in this surface as well as List **/
    val background2: Color,

    /** Tertiary surface, Mostly main navigation Eg. suite header, app bar. **/
    val background3: Color,

    /** Quaternary surface. For apps needing more surface hierarchy **/
    val background4: Color,

    /** Background button for button's container color **/
    val backgroundTouchable: Color,

    /** Background button for button's container color **/
    val backgroundTouchablePressed: Color,

    /** Inverted background surface Eg. inverted Toast notification. **/
    val backgroundInverted: Color,

    /** Background surface for disabled components **/
    val backgroundDisabled: Color,


    // Foreground surfaces

    /** Brand foreground Eg. clickable text, text on button **/
    val foreground: Color,

    /** Brand foreground pressed **/
    val foregroundPressed: Color,

    /** Primary foreground color Eg. text/icons on buttons **/
    val foreground1: Color,

    /** Secondary foreground color **/
    val foreground2: Color,

    /** Tertiary foreground color small headlines, categories in spinner **/
    val foreground3: Color,

    /** Quaternary foreground color Eg. placeholder text **/
    val foreground4: Color,

    /** Inverted foreground color Eg. inverted text on Toast **/
    val foregroundInverted: Color,

    /** Foreground for disabled components Eg. text/icon on disabled button **/
    val foregroundDisabled: Color,

    /** Foreground surface on brand colors **/
    val foregroundOnBrand: Color,

    /** Foreground for error text and icons **/
    val foregroundError: Color,

    // Stroke (Dividers/Border)

    /** Primary stroke color eg. primary button border, input field border **/
    val stroke1: Color,

    /** Secondary stroke color eg. page dividers **/
    val stroke2: Color,

    /** Stroke color for additional contrast, used in radio and checkboxes components. **/
    val strokeAccessible: Color,

    /** Stroke Disabled **/
    val strokeDisabled: Color,

    /** Brand Stroke **/
    val brandStroke: Color,

    internal val isDarkTheme: Boolean
)

val NewDarkColorScheme = NewColorScheme(
    backgroundBrand = Color(0xFF0C86FF),
    backgroundPressed = Color(0xFF046DD6),
    background1 = Color(0xFF26262C),
    background2 = Color(0xFF16161A),
    background3 = Color(0xFF1E1E24),
    background4 = Color(0xFF121016),
    backgroundInverted = Color.Transparent,
    backgroundDisabled = Color(0xff53596C), //
    backgroundTouchable = Color(0xFF26262C), //
    backgroundTouchablePressed = Color(0xFF393D4B),
    foreground = Color(0xFF0C86FF), //
    foregroundPressed = Color(0xFF046DD6), //
    foreground1 = Color(0xFFE9EBEE), //
    foreground2 = Color(0xFF97A4BA), //
    foreground3 = Color(0xFF646A7C), //
    foreground4 = Color(0xff4D515B),
    foregroundInverted = Color.White,
    foregroundDisabled = Color(0xff53596C), //
    foregroundOnBrand = Color.White,
    stroke1 = Color(0xFF666666),
    stroke2 = Color(0xFF2D2D2D), //
    strokeAccessible = Color(0xFFADADAD),
    strokeDisabled = Color(0xFF424242),
    brandStroke = Color(0xFF0C86FF), //
    foregroundError = Color(0xFFFF453A), //
    isDarkTheme = true
)

val NewLightColorScheme = NewColorScheme(
    backgroundBrand = Color(0xFF0C86FF), //
    backgroundPressed = Color(0xFF046DD6), //
    background1 = Color.White, //
    background2 = Color.White, //
    background3 = Color(0xFFf2f3f5), //
    background4 = Color(0xffe0e0e0),
    backgroundInverted = Color(0xFF292929),
    backgroundDisabled = Color(0xff808b9f), //
    backgroundTouchable = Color(0xFFF3F7FC), //
    backgroundTouchablePressed = Color(0xFFEBF2FC),
    foreground = Color(0xFF0C86FF),
    foregroundPressed = Color(0xFF046DD6),
    foreground1 = Color(0xff1d1d1d), //
    foreground2 = Color(0xff566379), //
    foreground3 = Color(0xff80889f), //
    foreground4 = Color(0xFF97A4BA), //
    foregroundInverted = Color.White,
    foregroundDisabled = Color(0xff97a4ba), //
    foregroundOnBrand = Color.White, //
    stroke1 = Color(0xFFD1D1D1),
    stroke2 = Color(0xffd5d5d5), //
    strokeAccessible = Color(0xFF808B9F), //
    strokeDisabled = Color(0xFFA8A8A8),
    brandStroke = Color(0xFF0C86FF), //
    foregroundError = Color(0xFFFF3B30), //
    isDarkTheme = false
)