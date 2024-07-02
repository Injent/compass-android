package ru.bgitu.feature.profile.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.bgitu.core.designsystem.components.AppConfirmDialog
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.profile.R

@Composable
internal fun SignOutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppConfirmDialog(
        modifier = modifier,
                title = stringResource(R.string.sign_out),
        text = stringResource(R.string.are_you_sure_you_want_to_sign_out),
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        dismissColor = AppTheme.colorScheme.foreground,
        confirmColor = AppTheme.colorScheme.foregroundError,
        confirmText = stringResource(R.string.sign_out)
    )
}