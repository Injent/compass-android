package ru.bgitu.feature.profile_settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppSwitch
import ru.bgitu.core.designsystem.components.AppSwitchTokens
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.ui.Headline
import ru.bgitu.feature.profile_settings.R

@Composable
fun PublicProfileOption(
    enabled: Boolean,
    onSwitch: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        Headline(
            text = stringResource(R.string.search),
            modifier = Modifier.padding(bottom = AppTheme.spacing.s)
        )
    }
    AppCard(
        modifier = modifier,
        onClick = { onSwitch(!enabled) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.public_profile),
                style = AppTheme.typography.body,
                color = AppTheme.colorScheme.foreground1
            )
            AppSwitch(
                checked = enabled,
                onCheckedChange = onSwitch
            )
        }
        Text(
            text = stringResource(R.string.public_profile_description),
            style = AppTheme.typography.callout,
            color = AppTheme.colorScheme.foreground2,
            modifier = Modifier
                .padding(
                    top = AppTheme.spacing.xs,
                    end = AppSwitchTokens.TrackWidth + AppTheme.spacing.m,
                )
        )
    }
}