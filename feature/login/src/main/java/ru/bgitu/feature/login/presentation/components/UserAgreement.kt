package ru.bgitu.feature.login.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.login.R

@Composable
internal fun UserAgreement(
    onUserAgreementClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val userAgreementText = buildAnnotatedString {
        val startIndex: Int
        val endIndex: Int
        val text = stringResource(R.string.user_agreement_text).let {
            startIndex = it.indexOf('{')
            endIndex = it.indexOf('}') - 1
            it.replace("{", "").replace("}", "")
        }
        withStyle(
            style = AppTheme.typography.callout.toSpanStyle().copy(
                color = AppTheme.colorScheme.foreground1
            )
        ) {
            append(text)
        }

        addStyle(
            style = AppTheme.typography.callout.toSpanStyle().copy(
                color = AppTheme.colorScheme.foreground,
                textDecoration = TextDecoration.Underline,
                baselineShift = BaselineShift.None
            ),
            start = startIndex,
            end = endIndex
        )

        addLink(
            start = startIndex,
            end = endIndex,
            clickable = LinkAnnotation.Clickable(
                tag = "user_agreement",
                linkInteractionListener = { onUserAgreementClick() }
            )
        )
    }
    Text(
        text = userAgreementText,
        style = AppTheme.typography.callout,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}