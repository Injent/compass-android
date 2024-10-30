package ru.bgitu.feature.profile_settings.presentation.components

import android.webkit.URLUtil
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import ru.bgitu.core.designsystem.components.AppConfirmButton
import ru.bgitu.core.designsystem.components.AppDialog
import ru.bgitu.core.designsystem.components.DynamicAsyncImage
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.ScrollBarConfig
import ru.bgitu.core.designsystem.util.horizontalScrollWithScrollbar
import ru.bgitu.feature.profile_settings.R

@Composable
internal fun ProfileImageInputDialog(
    modifier: Modifier = Modifier,
    onChangeAvatar: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current

    var profileUrl by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        profileUrl = clipboardManager.getText()?.text.orEmpty()

        isError = !URLUtil.isValidUrl(profileUrl)
    }

    AppDialog(
        title = stringResource(R.string.profile_photo),
        buttons = {
            AppConfirmButton(
                text = stringResource(android.R.string.ok),
                modifier = Modifier.weight(1f),
                onClick = {
                    if (isError) {
                        onDismiss()
                    } else {
                        onChangeAvatar(profileUrl)
                    }
                }
            )
        },
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.hint_avatar),
                style = AppTheme.typography.subheadline,
                color = AppTheme.colorScheme.foreground3
            )

            val profileImageModifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.5f)
                .aspectRatio(1.0f)
                .clip(CircleShape)

            when {
                profileUrl.isEmpty() -> {
                    ProfileImagePlaceholder(modifier = profileImageModifier)
                }
                isError -> {
                    ErrorClipboardImage(invalidBuffer = profileUrl)
                }
                else -> {
                    DynamicAsyncImage(
                        imageUrl = profileUrl,
                        modifier = profileImageModifier,
                        onResult = { isSuccess ->
                            isError = !isSuccess
                        }
                    )

                    Text(
                        text = stringResource(R.string.avatar_filled),
                        style = AppTheme.typography.footnote,
                        color = AppTheme.colorScheme.foreground3,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileImagePlaceholder(
    modifier: Modifier = Modifier
) {
    val pathEffect = remember {
        PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    }
    val outlineColor = AppTheme.colorScheme.stroke1
    val strokeWidthPx = LocalDensity.current.run { AppTheme.strokeWidth.large.toPx() }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .drawBehind {
                drawCircle(
                    color = outlineColor,
                    style = Stroke(
                        width = strokeWidthPx,
                        pathEffect = pathEffect
                    )
                )
            }
    ) {
        Text(
            text = stringResource(R.string.avatar_empty),
            style = AppTheme.typography.callout,
            color = AppTheme.colorScheme.foreground4,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.spacing.s),
        )
    }
}

@Composable
private fun ErrorClipboardImage(
    invalidBuffer: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs)
    ) {
        Text(
            text = stringResource(R.string.current_clipboard),
            style = AppTheme.typography.subheadline,
            color = AppTheme.colorScheme.foreground3,
        )

        val scrollState = rememberScrollState()

        Text(
            text = invalidBuffer,
            style = AppTheme.typography.body,
            color = AppTheme.colorScheme.foreground1,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScrollWithScrollbar(
                    state = scrollState,
                    scrollbarConfig = ScrollBarConfig(
                        indicatorThickness = 4.dp,
                        indicatorColor = AppTheme.colorScheme.foreground2
                    )
                )
                .background(AppTheme.colorScheme.background4, AppTheme.shapes.small)
                .padding(
                    horizontal = AppTheme.spacing.s,
                    vertical = AppTheme.spacing.s
                )
        )

        Text(
            text = stringResource(R.string.avatar_invalid),
            style = AppTheme.typography.footnote,
            color = AppTheme.colorScheme.foregroundError
        )
    }
}