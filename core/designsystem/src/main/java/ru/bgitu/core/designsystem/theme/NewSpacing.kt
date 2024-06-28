package ru.bgitu.core.designsystem.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class NewSpacing internal constructor(
    /** 2dp **/
    val xxs: Dp = 2.dp,
    /** 4dp **/
    val xs: Dp = 4.dp,
    /** 6dp **/
    val sNudge: Dp = 6.dp,
    /** 8dp **/
    val s: Dp = 8.dp,
    /** 10dp **/
    val mNudge: Dp = 10.dp,
    /** 12dp **/
    val m: Dp = 12.dp,
    /** 16dp **/
    val l: Dp = 16.dp,
    /** 20dp **/
    val xl: Dp = 20.dp,
    /** 24dp **/
    val xxl: Dp = 24.dp,
    /** 32dp **/
    val xxxl: Dp = 32.dp
)

internal val Spacing = NewSpacing()