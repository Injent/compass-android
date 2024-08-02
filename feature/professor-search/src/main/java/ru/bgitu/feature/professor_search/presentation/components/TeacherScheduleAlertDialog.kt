package ru.bgitu.feature.professor_search.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import ru.bgitu.core.designsystem.components.AppConfirmButton
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.professor_search.R
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun TeacherScheduleAlertDialog(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmationDelay by rememberSaveable { mutableIntStateOf(5) }

    LaunchedEffect(Unit) {
        while (confirmationDelay > 0) {
            delay(1.seconds)
            confirmationDelay--
        }
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {},
        confirmButton = {
            AppConfirmButton(
                text = stringResource(android.R.string.ok).let {
                    if (confirmationDelay > 0) {
                        "$it ($confirmationDelay)"
                    } else it
                },
                onClick = onConfirm,
                enabled = confirmationDelay == 0
            )
        },
        containerColor = AppTheme.colorScheme.background2,
        shape = AppTheme.shapes.default,
        title = {
            Text(
                text = stringResource(R.string.attention),
                style = AppTheme.typography.title3,
                color = AppTheme.colorScheme.foreground1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
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
    )
}