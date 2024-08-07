package ru.bgitu.feature.settings.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.bgitu.core.designsystem.components.AppConfirmButton
import ru.bgitu.core.designsystem.components.AppDialog
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.settings.R

@Composable
fun SelectGroupDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppDialog(
        title = stringResource(R.string.dialog_title_groupNotSelected),
        onDismissRequest = onDismiss,
        buttons = {
            AppConfirmButton(
                text = stringResource(R.string.action_select_group),
                onClick = onConfirm,
                modifier = Modifier.weight(1f)
            )
        },
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.dialog_group_descrition),
            style = AppTheme.typography.body,
            color = AppTheme.colorScheme.foreground1
        )
    }
}