package ru.bgitu.feature.notes.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppCheckBox
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun NoteItem(
    shortText: String,
    completed: Boolean,
    onClick: () -> Unit,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(
            horizontal = AppTheme.spacing.l,
            vertical = 14.dp
        )
    ) {
        var alignToTop by remember { mutableStateOf(false) }
        var canDraw by remember { mutableStateOf(false) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.m),
            verticalAlignment = if (alignToTop) Alignment.Top else Alignment.CenterVertically,
            modifier = Modifier.drawWithContent { if (canDraw) drawContent() }
        ) {
            AppCheckBox(
                checked = completed,
                onCheckedChange = onChecked
            )
            Text(
                text = shortText,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                onTextLayout = { result ->
                    alignToTop = result.didOverflowWidth
                    canDraw = true
                },
                style = AppTheme.typography.headline2,
                color = AppTheme.colorScheme.foreground1,
                textDecoration = if (completed) TextDecoration.LineThrough else null
            )
        }
    }
}