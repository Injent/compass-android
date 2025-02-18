package ru.bgitu.feature.notes.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Copy
import ru.bgitu.core.designsystem.icon.Share
import ru.bgitu.core.designsystem.icon.Trash
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.notes.R
import ru.bgitu.feature.notes.presentation.details.NoteDetailsIntent

@Composable
internal fun EditorActionsBottomSheet(
    onDismissRequest: () -> Unit,
    onIntent: (NoteDetailsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        shape = AppTheme.shapes.default,
        sheetState = sheetState,
        dragHandle = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.spacing.l)
        ) {
            EditorAction(
                icon = AppIcons.Trash,
                label = stringResource(R.string.action_delete),
                onClick = {
                    onIntent(NoteDetailsIntent.Delete)
                }
            )
            EditorAction(
                icon = AppIcons.Copy,
                label = stringResource(R.string.action_copy),
                onClick = {
                    onIntent(NoteDetailsIntent.Copy)
                }
            )
            EditorAction(
                icon = AppIcons.Share,
                label = stringResource(R.string.action_share),
                onClick = {
                    onIntent(NoteDetailsIntent.Share)
                }
            )
        }
    }
}

@Composable
fun EditorAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppRippleTheme {
        Surface(
            color = Color.Transparent,
            onClick = onClick,
            shape = AppTheme.shapes.default
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = label,
                    style = AppTheme.typography.calloutButton,
                    color = AppTheme.colorScheme.foreground1
                )
            }
        }
    }
}