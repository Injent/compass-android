package ru.bgitu.feature.profile_settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Telegram
import ru.bgitu.core.designsystem.icon.Vk
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.model.Contacts
import ru.bgitu.core.ui.Headline
import ru.bgitu.feature.profile_settings.R

@Composable
fun ContactOption(
    contacts: Contacts,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Headline(
            text = stringResource(R.string.contacts),
            modifier = Modifier.padding(bottom = AppTheme.spacing.s)
        )
        AppCard {
            contacts.tg?.let {
                ContactEntry(
                    icon = AppIcons.Telegram,
                    contactUrl = it,
                    onClick = { }
                )
            }
            contacts.vk?.let {
                ContactEntry(
                    icon = AppIcons.Vk,
                    contactUrl = it,
                    onClick = { }
                )
            }
        }
    }
}

@Composable
private fun ContactEntry(
    icon: ImageVector,
    contactUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent,
        shape = AppTheme.shapes.default,
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            val contactName = remember {
                contactUrl.substringAfterLast('/')
            }
            Text(
                text = contactName,
                style = AppTheme.typography.body,
                color = AppTheme.colorScheme.foreground
            )
        }
    }
}