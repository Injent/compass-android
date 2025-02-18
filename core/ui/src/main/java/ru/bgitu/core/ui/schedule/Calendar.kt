package ru.bgitu.core.ui.schedule

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun CalendarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            surface = AppTheme.colorScheme.background1,
            onSurface = AppTheme.colorScheme.foreground1,
            primary = AppTheme.colorScheme.foreground,
            onPrimary = AppTheme.colorScheme.foregroundOnBrand,
            secondaryContainer = AppTheme.colorScheme.backgroundBrand,
            surfaceVariant = AppTheme.colorScheme.background4
        ),
        content = content
    )
}