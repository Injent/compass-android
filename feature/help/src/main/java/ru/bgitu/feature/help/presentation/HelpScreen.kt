package ru.bgitu.feature.help.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.designsystem.components.AppFilledIconButton
import ru.bgitu.core.designsystem.components.DynamicAsyncImage
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.feature.help.R
import ru.bgitu.feature.help.model.DevContact

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
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(vertical = AppTheme.spacing.l)
        ) {
            val devContacts = DevContact.entries.shuffled()
            devContacts.forEach { devContact ->
                DeveloperContact(
                    fullName = stringResource(devContact.nameResId),
                    imageUrl = devContact.avatarUrl,
                    role = stringResource(devContact.roleResId),
                    description = stringResource(devContact.descriptionResId),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (social in devContact.socials) {
                        AppFilledIconButton(
                            onClick = { onClickUrl(social.url) },
                            icon = social.iconResId,
                            tint = Color.Unspecified
                        )
                    }
                }
                if (devContacts.last() != devContact) {
                    Spacer(Modifier.height(AppTheme.spacing.xxxl))
                }
            }
        }
    }
}

@Composable
private fun DeveloperContact(
    modifier: Modifier = Modifier,
    fullName: String,
    imageUrl: String,
    role: String,
    description: String,
    contacts: @Composable RowScope.() -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DynamicAsyncImage(
            imageUrl = imageUrl,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(32.dp))
        )
        Text(
            text = fullName,
            style = AppTheme.typography.headline1,
            color = AppTheme.colorScheme.foreground1,
            modifier = Modifier
                .padding(
                    top = AppTheme.spacing.s,
                    bottom = AppTheme.spacing.xs
                )
        )
        Text(
            text = role,
            style = AppTheme.typography.footnote,
            color = AppTheme.colorScheme.foreground2,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.6f),
        )
        Text(
            text = description,
            style = AppTheme.typography.callout,
            color = AppTheme.colorScheme.foreground1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(
                    top = AppTheme.spacing.xs,
                    bottom = AppTheme.spacing.s
                )
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xxxl),
            content = contacts
        )
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