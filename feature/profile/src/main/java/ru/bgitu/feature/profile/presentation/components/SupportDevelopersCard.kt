package ru.bgitu.feature.profile.presentation.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import ru.bgitu.core.common.TELEGRAM_BOT_DOMAIN
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.profile.R

@Composable
fun SupportDevelopersCard(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    AppCard(
        modifier = modifier,
        onClick = {
            context.launchTelegramBotSupportFeature()
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(AppIcons.Support),
                contentDescription = null,
                tint = AppTheme.colorScheme.foreground,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(R.string.support_devs),
                style = AppTheme.typography.headline1,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(AppIcons.Link),
                contentDescription = null,
                tint = AppTheme.colorScheme.foreground,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private fun Context.launchTelegramBotSupportFeature() {
    try {
        Intent(
            ACTION_VIEW,
            "tg://resolve?domain=$TELEGRAM_BOT_DOMAIN&start=support_words".toUri()
        ).also(::startActivity)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            this,
            ru.bgitu.components.signin.R.string.telegram_appNotFound,
            Toast.LENGTH_SHORT
        ).show()
    }
}