package ru.bgitu.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.bgitu.core.designsystem.components.AppRippleButton
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun AppSearchItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppRippleButton(
        text = label,
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = AppTheme.spacing.xxxl),
        modifier = modifier
    )
}