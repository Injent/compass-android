package ru.bgitu.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppIconButton
import ru.bgitu.core.designsystem.icon.AppIcons

@Composable
fun AppBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppIconButton(
        onClick = onClick,
        icon = AppIcons.Back,
        iconSize = 24.dp,
        modifier = modifier
    )
}