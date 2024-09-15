package ru.bgitu.feature.update.presentation

import android.text.format.Formatter
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.Coil
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.designsystem.components.AppButton
import ru.bgitu.core.designsystem.components.AppCircularLoading
import ru.bgitu.core.designsystem.components.AppSecondaryButton
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.util.ScrollBarConfig
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.core.designsystem.util.verticalScrollWithScrollbar
import ru.bgitu.feature.update.R
import ru.bgitu.feature.update.model.AppUpdateSheetData
import ru.bgitu.feature.update.model.AppUpdateUiState

@Composable
fun AppUpdateBottomSheet(
    sheetData: AppUpdateSheetData,
    onDismissRequest: () -> Unit,
) {
    val viewModel: AppUpdateViewModel = koinViewModel {
        parametersOf(sheetData.availableVersionCode)
    }
    val changelog by viewModel.changelog.collectAsStateWithLifecycle()

    val uiState by viewModel.uiState.collectAsState()

    AppUpdateBottomSheetDialogContent(
        onUpdateRequest = viewModel::onUpdateRequest,
        onUpdateDismiss = onDismissRequest,
        changelog = changelog,
        sheetData = sheetData,
        uiState = uiState
    )
}

@Composable
private fun AppUpdateBottomSheetDialogContent(
    onUpdateRequest: () -> Unit,
    onUpdateDismiss: () -> Unit,
    sheetData: AppUpdateSheetData,
    changelog: String?,
    uiState: AppUpdateUiState,
) {
    Surface(
        color = AppTheme.colorScheme.background1,
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(
                    horizontal = AppTheme.spacing.xl,
                    vertical = AppTheme.spacing.l
                )
        ) {
            DialogHeader(
                onDismiss = onUpdateDismiss,
                dismissible = false,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(Modifier.fillMaxWidth(), color = AppTheme.colorScheme.stroke2)
            DialogBody(
                sizeBytes = sheetData.sizeBytes,
                changelog = changelog,
                forced = true,
                uiState = uiState
            )
            DialogActions(
                onUpdateDismiss = onUpdateDismiss,
                onUpdateRequest = onUpdateRequest,
                dissmissable = false
            )
        }
    }
}

@Composable
private fun DialogHeader(
    onDismiss: () -> Unit,
    dismissible: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = AppTheme.spacing.l)
    ) {
        Text(
            text = stringResource(R.string.app_update),
            style = AppTheme.typography.title3,
            color = AppTheme.colorScheme.foreground1
        )
        if (dismissible) {
            Icon(
                painter = painterResource(AppIcons.CloseSmall),
                contentDescription = null,
                tint = AppTheme.colorScheme.foreground3,
                modifier = Modifier.clickable { onDismiss() }
            )
        }
    }
}

@Composable
private fun DialogBody(
    sizeBytes: Long,
    changelog: String?,
    forced: Boolean,
    uiState: AppUpdateUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(vertical = AppTheme.spacing.l)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l)
        ) {
            Image(
                painter = painterResource(ru.bgitu.core.common.R.drawable.app_logo),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Column {
                Text(
                    text = stringResource(
                        if (forced) R.string.force_update_confirmation_title
                        else R.string.update_confirmation_title
                    ),
                    style = AppTheme.typography.headline1,
                    color = AppTheme.colorScheme.foreground1
                )
                Text(
                    text = stringResource(
                        R.string.update_size,
                        Formatter.formatFileSize(context, sizeBytes)
                    ),
                    style = AppTheme.typography.callout,
                    color = AppTheme.colorScheme.foreground2
                )
            }
        }
        uiState.errorText?.let { errorText ->
            Spacer(Modifier.height(AppTheme.spacing.l))
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l)
            ) {
                Icon(
                    painter = painterResource(AppIcons.WarningRed),
                    contentDescription = null,
                    modifier = Modifier
                        .size(AppTheme.spacing.xl)
                        .offset(y = 3.dp),
                    tint = Color.Unspecified
                )
                Text(
                    text = errorText.asString(),
                    style = AppTheme.typography.body,
                    color = AppTheme.colorScheme.foregroundError
                )
            }
        }
        Spacer(Modifier.height(AppTheme.spacing.xxxl))

        if (changelog != null) {
            val changelogScrollState = rememberScrollState()
            Column(
                Modifier
                    .height(250.dp)
                    .verticalScrollWithScrollbar(
                        changelogScrollState,
                        scrollbarConfig = ScrollBarConfig(
                            indicatorColor = AppTheme.colorScheme.foreground4,
                            alpha = 0.5f,
                            indicatorThickness = 5.dp
                        )
                    )
            ) {
                MarkdownText(
                    markdown = changelog,
                    color = AppTheme.colorScheme.foreground1,
                    linkColor = AppTheme.colorScheme.foreground,
                    isTextSelectable = false,
                    onLinkClicked = context::openUrl,
                    style = AppTheme.typography.body,
                    imageLoader = Coil.imageLoader(context),
                )
            }
        } else {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                AppCircularLoading(
                    modifier = Modifier.align(Alignment.Center),
                    tint = AppTheme.colorScheme.foreground
                )
            }
        }
    }
}

@Composable
private fun DialogActions(
    onUpdateDismiss: () -> Unit,
    onUpdateRequest: () -> Unit,
    dissmissable: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s)
    ) {
        AppButton(
            text = stringResource(R.string.update),
            onClick = onUpdateRequest,
            modifier = Modifier.fillMaxWidth()
        )
        if (dissmissable) {
            AppSecondaryButton(
                text = stringResource(R.string.later),
                onClick = onUpdateDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun AppUpdateDialogPreview() {
    CompassTheme {
        AppUpdateBottomSheetDialogContent(
            onUpdateRequest = {},
            onUpdateDismiss = {},
            changelog = "New Features",
            sheetData = AppUpdateSheetData(
                sizeBytes = 10000000,
                forced = false,
                availableVersionCode = 0
            ),
            uiState = AppUpdateUiState()
        )
    }
}