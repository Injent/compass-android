package ru.bgitu.feature.about.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.Coil
import dev.jeziellago.compose.markdowntext.MarkdownText
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.designsystem.R
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun Changelog(
    markdown: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    MarkdownText(
        markdown = markdown,
        color = AppTheme.colorScheme.foreground1,
        linkColor = AppTheme.colorScheme.foreground,
        isTextSelectable = false,
        onLinkClicked = context::openUrl,
        style = AppTheme.typography.body,
        imageLoader = Coil.imageLoader(context),
        fontResource = R.font.alshauss_regular,
        modifier = modifier
    )
}