package ru.bgitu.feature.help.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.bgitu.core.common.BuildConfig
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppIconButton
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Telegram
import ru.bgitu.core.designsystem.icon.Vk
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.designsystem.theme.start
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.feature.help.R

@Composable
fun HelpScreen() {
    val navController = LocalNavController.current
    val context = LocalContext.current

    HelpScreenContent(
        onBack = { navController.back() },
        onClickUrl = context::openUrl
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HelpScreenContent(
    onBack: () -> Unit,
    onClickUrl: (String) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.help_title),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1
                    )
                },
                navigationIcon = {
                    AppBackButton(onClick = onBack)
                }
            )
        },
        modifier = Modifier
            .padding(start = LocalExternalPadding.current.start),
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(AppTheme.spacing.l)
        ) {
            AppCard {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.chatOfTechSupport),
                        color = AppTheme.colorScheme.foreground1,
                        style = AppTheme.typography.headline1
                    )
                    Spacer(Modifier.weight(1f))
                    AppIconButton(
                        onClick = {
                            onClickUrl("${BuildConfig.COMPASS_SITE}/contacts/eliseyVerevkin/tg")
                        },
                        icon = AppIcons.Telegram,
                        tint = Color.Unspecified,
                        iconSize = 24.dp
                    )
                    AppIconButton(
                        onClick = {
                            onClickUrl("${BuildConfig.COMPASS_SITE}/contacts/eliseyVerevkin/vk")
                        },
                        icon = AppIcons.Vk,
                        tint = Color.Unspecified,
                        iconSize = 24.dp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 1, locale = "ru")
@Composable
private fun HelpScreenContentPreview() {
    CompassTheme {
        HelpScreenContent(
            onBack = {},
            onClickUrl = {}
        )
    }
}