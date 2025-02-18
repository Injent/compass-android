package ru.bgitu.feature.settings.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.settings.R

@Composable
fun ExpandableDetails(
    expanded: Boolean,
    onExpand: () -> Unit,
    description: AnnotatedString,
    modifier: Modifier = Modifier,
    buttonText: String = stringResource(R.string.more_detailed),
) {
    Column(modifier) {
        val detailsArrowRotation by animateFloatAsState(
            targetValue = if (expanded) 90f else -90f
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.spacing.s)
                .padding(vertical = AppTheme.spacing.xxs)
                .clickable { onExpand() }
        ) {
            Text(
                text = buttonText,
                style = AppTheme.typography.calloutButton,
                color = AppTheme.colorScheme.foreground3
            )
            Icon(
                painter = painterResource(AppIcons.SmallArrowNext),
                contentDescription = null,
                tint = AppTheme.colorScheme.foreground3,
                modifier = Modifier
                    .size(12.dp)
                    .rotate(detailsArrowRotation)
            )
        }

        AnimatedVisibility(
            visible = expanded,
            modifier = Modifier.clickable { onExpand() }
        ) {
            Text(
                text = description,
                style = AppTheme.typography.footnote,
                color = AppTheme.colorScheme.foreground2
            )
        }
    }
}