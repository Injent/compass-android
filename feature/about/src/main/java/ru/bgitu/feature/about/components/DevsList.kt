package ru.bgitu.feature.about.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppIconButton
import ru.bgitu.core.designsystem.components.DynamicAsyncImage
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.about.model.DevContact

@Composable
fun DevsList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s)
    ) {
        DevContact.entries.forEach { contact ->
            DevCard(
                contact = contact,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 300.dp)
            )
        }
    }
}

@Composable
fun DevCard(
    contact: DevContact,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AppCard(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            modifier = Modifier
        ) {
            DynamicAsyncImage(
                imageUrl = contact.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(contact.nameResId),
                    color = AppTheme.colorScheme.foreground1,
                    style = AppTheme.typography.headline2
                )
                Text(
                    text = stringResource(contact.roleResId),
                    color = AppTheme.colorScheme.foreground2,
                    style = AppTheme.typography.caption1,
                    maxLines = 2
                )
            }
            contact.socials.forEach { social ->
                AppIconButton(
                    onClick = { context.openUrl(social.url) },
                    icon = social.icon,
                    tint = Color.Unspecified,
                    iconSize = 24.dp
                )
            }
        }
    }
}