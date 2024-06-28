package ru.bgitu.feature.groups.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.bgitu.core.designsystem.components.AppConfirmDialog
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.model.Group
import ru.bgitu.feature.groups.R

@Composable
internal fun DeleteGroupDialog(
    group: Group,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppConfirmDialog(
        title = stringResource(R.string.delete_group),
        text = stringResource(R.string.delete_group_description, group.name),
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        confirmColor = AppTheme.colorScheme.foregroundError,
        confirmText = stringResource(R.string.delete),
        cancelText = stringResource(android.R.string.cancel),
        dismissColor = AppTheme.colorScheme.foreground,
        modifier = modifier
    )
}