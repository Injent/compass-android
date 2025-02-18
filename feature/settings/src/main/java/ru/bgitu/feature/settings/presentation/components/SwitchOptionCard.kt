package ru.bgitu.feature.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppSwitch
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
internal fun SwitchOptionCard(
    label: String,
    checked: Boolean,
    onSwitch: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(
        onClick = { onSwitch(!checked) },
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = label,
                style = AppTheme.typography.headline2,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = AppTheme.spacing.l)
            )
            AppSwitch(
                checked = checked,
                onCheckedChange = onSwitch,
                modifier = Modifier
            )
        }
    }
}