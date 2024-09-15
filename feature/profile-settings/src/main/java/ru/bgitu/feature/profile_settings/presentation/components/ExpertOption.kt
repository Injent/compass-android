package ru.bgitu.feature.profile_settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppCardWithContent
import ru.bgitu.core.designsystem.components.Status
import ru.bgitu.core.designsystem.components.StatusDecor
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.MeasureComposable
import ru.bgitu.feature.profile_settings.R

private const val INLINE_TEXT_EXPERT = "status_expert"

@Composable
internal fun ExpertOption(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCardWithContent(
        modifier = modifier,
        label = stringResource(R.string.expert_label),
        action = stringResource(R.string.action_become_an_expert),
        onClick = onClick
    ) {
        ExpertDescriptionText()
    }
}

@Composable
private fun ExpertDescriptionText(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val expertDescription = buildAnnotatedString {
        append(stringResource(R.string.expert_description))
        append(' ')
        appendInlineContent(INLINE_TEXT_EXPERT)
    }

    MeasureComposable(
        modifier = modifier,
        composable = {
            InlineTextExpertStatus()
        }
    ) { size ->
        Text(
            text = expertDescription,
            inlineContent = remember {
                getTextInlineContent(
                    width = density.run { size.width.toSp() },
                    height = density.run { size.height.toSp() }
                )
            },
            color = AppTheme.colorScheme.foreground2,
            style = AppTheme.typography.callout,
            modifier = Modifier
        )
    }
}

private fun getTextInlineContent(width: TextUnit, height: TextUnit) = mapOf(
    INLINE_TEXT_EXPERT to InlineTextContent(
        placeholder = Placeholder(
            width = width,
            height = height,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
        )
    ) {
        InlineTextExpertStatus()
    }
)

@Composable
private fun InlineTextExpertStatus(
    modifier: Modifier = Modifier
) {
    val contentColor = AppTheme.colorScheme.statusExpert

    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                color = contentColor.copy(.15f),
                shape = CircleShape
            )
            .padding(
                horizontal = AppTheme.spacing.xs,
                vertical = AppTheme.spacing.xxs
            )
    ) {
        Icon(
            painter = painterResource(AppIcons.ThumbUp),
            contentDescription = null,
            modifier = Modifier.size(10.dp),
            tint = contentColor,
        )
        Text(
            text = stringResource(ru.bgitu.core.designsystem.R.string.status_expert),
            style = AppTheme.typography.footnote,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}