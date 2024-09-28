package ru.bgitu.feature.settings.presentation.components

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
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.ui.Headline
import ru.bgitu.feature.settings.R

@Composable
fun OtherSettingsGroup(
    helpTraffic: Boolean,
    onSwitchTraffic: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Headline(
            text = stringResource(R.string.group_miscellaneous),
            modifier = Modifier.padding(bottom = AppTheme.spacing.mNudge)
        )

        AppCard(
            onClick = { onSwitchTraffic(!helpTraffic) }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.help_site_traffic),
                    style = AppTheme.typography.headline2,
                    color = AppTheme.colorScheme.foreground1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = AppTheme.spacing.l)
                )
                AppSwitch(
                    checked = helpTraffic,
                    onCheckedChange = onSwitchTraffic,
                    modifier = modifier
                )
            }
        }
    }
}