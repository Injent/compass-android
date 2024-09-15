package ru.bgitu.feature.profile_settings.presentation.expert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppButton
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.AutoSizeText
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.feature.profile_settings.R
import ru.bgitu.feature.profile_settings.presentation.components.ExpertAgreement

@Composable
fun ExpertApplyRoute() {
    val navController = LocalNavController.current
    val viewModel = koinViewModel<ExpertApplyViewModel>()

    val agreementChecked by viewModel.agreementChecked.collectAsStateWithLifecycle()

    ExpertApplyScreen(
        agreementChecked = agreementChecked,
        onCheckAgreement = viewModel::onCheckAgreement,
        onBack = navController::back
    )
}

@Composable
private fun ExpertApplyScreen(
    agreementChecked: Boolean,
    onCheckAgreement: (Boolean) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        contentWindowInsets = WindowInsets(bottom = 0),
        topBar = {
            TopAppBar(
                title = {
                    AutoSizeText(
                        text = stringResource(R.string.title_iWantToBecomeAnExpert),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1,
                    )
                },
                navigationIcon = {
                    AppBackButton(onClick = onBack)
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            contentPadding = PaddingValues(
                top = AppTheme.spacing.l,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppTheme.spacing.l)
        ) {
            (1..5).forEach { paragraph ->
                item(key = paragraph) {
                    val title = remember {
                        "$paragraph. " + context.getString(
                            context.resources.getIdentifier(
                                "expert_paragraph${paragraph}_title",
                                "string",
                                context.packageName
                            )
                        )
                    }
                    val text = remember {
                        context.getString(
                            context.resources.getIdentifier(
                                "expert_paragraph${paragraph}_text",
                                "string",
                                context.packageName
                            )
                        )
                    }

                    TextParagraph(
                        title = title,
                        text = text
                    )
                }
            }

            item {
                ExpertAgreement(
                    checked = agreementChecked,
                    onCheckedChange = onCheckAgreement
                )
            }

            item {
                AppButton(
                    enabled = agreementChecked,
                    text = stringResource(R.string.action_confirm),
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = AppTheme.spacing.l)
                )
            }
        }
    }
}

@Composable
private fun TextParagraph(title: String, text: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                AppTheme.typography.headline1.toSpanStyle()
                    .copy(color = AppTheme.colorScheme.foreground1)
            ) {
                append(title)
            }
            appendLine()
            withStyle(
                AppTheme.typography.body.toSpanStyle()
                    .copy(color = AppTheme.colorScheme.foreground2)
            ) {
                append(text)
            }
        }
    )
}