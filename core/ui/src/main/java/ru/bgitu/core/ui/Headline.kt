package ru.bgitu.core.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun Headline(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = AppTheme.typography.subheadline,
        color = AppTheme.colorScheme.foreground2,
        modifier = modifier
    )
}