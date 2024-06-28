package ru.bgitu.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.bgitu.core.designsystem.components.AppConfirmDialog
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onLogoutRequest: () -> Unit
) {
    AppConfirmDialog(
        title = stringResource(R.string.logout_title),
        text = stringResource(R.string.logout_description),
        onConfirm = onLogoutRequest,
        onDismiss = onDismiss,
        dismissColor = AppTheme.colorScheme.foreground,
        confirmColor = AppTheme.colorScheme.foregroundError,
        confirmText = stringResource(R.string.logout),
    )
}