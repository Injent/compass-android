package ru.bgitu.feature.settings.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
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

        SwitchOptionCard(
            label = stringResource(R.string.help_site_traffic),
            checked = helpTraffic,
            onSwitch = onSwitchTraffic
        )

        var showDetails by remember { mutableStateOf(false) }
        ExpandableDetails(
            expanded = showDetails,
            onExpand = { showDetails = !showDetails },
            description = buildAnnotatedString {
                append(stringResource(R.string.site_traffic_details))
            }
        )
    }
}