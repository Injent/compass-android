package ru.bgitu.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppConfirmDialog
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.profile.R

@Composable
fun TryNewFeatureDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppConfirmDialog(
        title = stringResource(R.string.try_find_mates),
        onConfirm = onConfirm,
        onDismiss = onDismissRequest,
        modifier = modifier
    ) {
        Text(
            text = buildAnnotatedString {
                val str = StringBuilder(stringResource(R.string.new_feature_dialog_text))

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
            color = AppTheme.colorScheme.foreground1,
            modifier = Modifier.padding(horizontal = AppTheme.spacing.m)
        )
        Spacer(Modifier.height(AppTheme.spacing.s))
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.m),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = AppTheme.colorScheme.background1,
                    shape = AppTheme.shapes.small
                )
                .padding(AppTheme.spacing.mNudge)
        ) {
            Icon(
                painter = painterResource(AppIcons.WarningRed),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(R.string.new_feature_dialog_text_warning),
                style = AppTheme.typography.subheadline,
                color = AppTheme.colorScheme.foreground1
            )
        }
    }
}