package ru.bgitu.feature.professor_search.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import ru.bgitu.core.designsystem.components.AppConfirmButton
import ru.bgitu.core.designsystem.components.AppDialog
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.professor_search.R

@Composable
internal fun TeacherScheduleAlertDialog(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppDialog(
        modifier = modifier,
        title = stringResource(R.string.attention),
        onDismissRequest = {},
        buttons = {
            AppConfirmButton(
                text = stringResource(android.R.string.ok),
                onClick = onConfirm,
                modifier = Modifier.weight(1f)
            )
        }
    ) {
        Text(
            text = buildAnnotatedString {
                val str = StringBuilder(stringResource(R.string.alert_text))

                while('{' in str) {
                    val startIndex = str.indexOfFirst { it == '{' }
                    val endIndex = str.indexOfFirst { it == '}' } - 1

                    addStyle(
                        style = SpanStyle(color = AppTheme.colorScheme.backgroundBrand),
                        start = startIndex,
                        end = endIndex
                    )
                    str.deleteCharAt(startIndex).deleteCharAt(endIndex)
                }

                append(str)
            },
            style = AppTheme.typography.body,
            color = AppTheme.colorScheme.foreground1
        )
    }
}