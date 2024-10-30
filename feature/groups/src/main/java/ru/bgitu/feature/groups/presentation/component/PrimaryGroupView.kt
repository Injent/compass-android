package ru.bgitu.feature.groups.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Swap
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.groups.R

@Composable
internal fun PrimaryGroupItem(
    groupName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(
        color = AppTheme.colorScheme.backgroundBrand,
        shape = AppTheme.shapes.large,
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        AppTheme.typography.headline2.toSpanStyle()
                    ) {
                        appendLine(groupName)
                    }
                    append(stringResource(R.string.label_primary_group))
                },
                style = AppTheme.typography.callout,
                color = AppTheme.colorScheme.foregroundOnBrand
            )
            Icon(
                imageVector = AppIcons.Swap,
                contentDescription = null,
                tint = AppTheme.colorScheme.foregroundOnBrand
            )
        }
    }
}