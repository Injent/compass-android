package ru.bgitu.core.designsystem.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class AppStrokeWidth internal constructor(
    val thin: Dp = 1.2.dp,
    val thick: Dp = 1.5.dp,
    val thicker: Dp = 2.dp,
)

internal val StrokeWidth = AppStrokeWidth()