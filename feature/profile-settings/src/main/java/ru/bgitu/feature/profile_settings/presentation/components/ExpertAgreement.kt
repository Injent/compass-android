package ru.bgitu.feature.profile_settings.presentation.components

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.net.toUri
import ru.bgitu.core.common.USER_AGREEMENT_URL
import ru.bgitu.core.designsystem.components.AppCheckBox
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.profile_settings.R

@Composable
fun ExpertAgreement(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.mNudge),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AppCheckBox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = buildAnnotatedString {
                val str = StringBuilder(stringResource(R.string.expert_agreement))

                val startIndex = str.indexOfFirst { it == '{' }
                val endIndex = str.indexOfFirst { it == '}' } - 1

                str.deleteCharAt(startIndex).deleteCharAt(endIndex)

                addLink(
                    url = LinkAnnotation.Url(
                        url = USER_AGREEMENT_URL,
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = AppTheme.colorScheme.foreground,
                                textDecoration = TextDecoration.Underline
                            )
                        ),
                        linkInteractionListener = {
                            context.openUserAgreement()
                        }
                    ),
                    start = startIndex,
                    end = endIndex
                )

                append(str)
            },
            textAlign = TextAlign.Center,
            style = AppTheme.typography.callout,
            color = AppTheme.colorScheme.foreground1
        )
    }
}

private fun Context.openUserAgreement() {
    Intent(ACTION_VIEW).apply {
        data = USER_AGREEMENT_URL.toUri()
    }.also(::startActivity)
}