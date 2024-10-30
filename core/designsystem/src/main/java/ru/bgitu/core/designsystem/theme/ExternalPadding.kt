package ru.bgitu.core.designsystem.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

val LocalExternalPadding = staticCompositionLocalOf {
    PaddingValues(0.dp)
}

val PaddingValues.start: Dp
    get() = calculateLeftPadding(LayoutDirection.Ltr)

val PaddingValues.end: Dp
    get() = calculateRightPadding(LayoutDirection.Ltr)

val PaddingValues.bottom: Dp
    get() = calculateBottomPadding()