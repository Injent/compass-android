package ru.bgitu.feature.about.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.common.DOWNLOAD_APP_URL
import ru.bgitu.core.common.USER_AGREEMENT_URL
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.common.versionCode
import ru.bgitu.core.common.versionName
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.feature.about.R
import ru.bgitu.feature.about.components.Changelog

@Composable
fun AboutScreen() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val viewModel: AboutViewModel = koinViewModel()

    val changelog by viewModel.changelog.collectAsStateWithLifecycle()

    AboutScreenContent(
        onBack = { navController.back() },
        versionName = "${context.versionName} (${context.versionCode})",
        changelog = changelog,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreenContent(
    onBack: () -> Unit,
    versionName: String,
    changelog: String?
) {
    val context = LocalContext.current

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.about_app),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1
                    )
                },
                navigationIcon = {
                    AppBackButton(
                        onClick = onBack,
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + AppTheme.spacing.l
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = AppTheme.spacing.l),
        ) {
            item {
                Spacer(Modifier.height(56.dp))
            }
            item {
                Image(
                    painter = painterResource(ru.bgitu.core.common.R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(Modifier.height(AppTheme.spacing.xxxl))
                Text(
                    text = versionName,
                    style = AppTheme.typography.callout,
                    color = AppTheme.colorScheme.foreground2
                )
                Spacer(Modifier.height(AppTheme.spacing.xxxl))
                Text(
                    text = stringResource(R.string.app_description),
                    style = AppTheme.typography.body,
                    color = AppTheme.colorScheme.foreground1,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(AppTheme.spacing.l))

                Text(
                    text = buildAnnotatedString {
                        withLink(
                            link = LinkAnnotation.Clickable(
                                tag = "click_user_agreement",
                                linkInteractionListener = { context.openUrl(USER_AGREEMENT_URL) }
                            )
                        ) {
                            append(context.getString(R.string.user_agreement))
                        }
                    },
                    color = AppTheme.colorScheme.foreground,
                    fontWeight = FontWeight.SemiBold,
                    style = AppTheme.typography.callout
                )

                Spacer(Modifier.height(AppTheme.spacing.l))

                Text(
                    text = buildAnnotatedString {
                        withLink(
                            link = LinkAnnotation.Clickable(
                                tag = "click_share",
                                linkInteractionListener = { context.shareAppLink() }
                            )
                        ) {
                            append(context.getString(R.string.share_app))
                        }
                    },
                    color = AppTheme.colorScheme.foreground,
                    fontWeight = FontWeight.SemiBold,
                    style = AppTheme.typography.callout
                )
            }
            changelog?.let {
                item {
                    Spacer(Modifier.height(AppTheme.spacing.xxxl))
                    Text(
                        text = stringResource(R.string.changelogs),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1
                    )
                    Spacer(Modifier.height(AppTheme.spacing.l))
                    Changelog(markdown = it)
                    Spacer(modifier = Modifier.height(AppTheme.spacing.l))
                }
            }
        }
    }
}

private fun Context.shareAppLink() {
    val share = Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message, DOWNLOAD_APP_URL))
    }, "")
    startActivity(share)
}

@Preview(showBackground = true, backgroundColor = 1)
@Composable
private fun AboutScreenContentPreview() {
    CompassTheme {
        AboutScreenContent(
            onBack = {},
            versionName = "1.0-Release",
            changelog = null
        )
    }
}