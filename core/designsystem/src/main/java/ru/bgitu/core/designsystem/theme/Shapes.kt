package ru.bgitu.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

val Shapes = AppShapes(
    default = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp),
    defaultTopCarved = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    defaultBottomCarved = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
    extraSmall = RoundedCornerShape(4.dp)
)