package ru.bgitu.core.ui.schedule

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun CalendarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            surface = AppTheme.colorScheme.background1,
            onSurface = AppTheme.colorScheme.foreground1,
            primary = AppTheme.colorScheme.foreground,
            secondaryContainer = AppTheme.colors.blue1,
            onSecondaryContainer = Color.White
        ),
        content = content
    )
}