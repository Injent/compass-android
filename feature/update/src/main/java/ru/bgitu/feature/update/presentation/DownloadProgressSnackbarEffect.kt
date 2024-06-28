package ru.bgitu.feature.update.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import ru.bgitu.components.updates.api.model.InstallState
import ru.bgitu.core.designsystem.components.LocalSnackbarController
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.asString
import ru.bgitu.feature.update.R

@Composable
fun DownloadProgressSnackbarEffect(
    installState: InstallState,
    onUpdateRequest: () -> Unit,
    onRetryRequest: () -> Unit
) {
    val snackbarController = LocalSnackbarController.current
    val context = LocalContext.current

    var downloadingInit by remember { mutableStateOf(false) }

    val progressMessage = remember { mutableStateOf("") }
    val withDismissAction = remember { mutableStateOf(false) }
    val icon = remember { mutableIntStateOf(AppIcons.WarningRed) }
    val actionLabel = remember { mutableStateOf<String?>(null) }
    val whiteTint = AppTheme.colorScheme.foregroundOnBrand
    val iconTint = remember { mutableStateOf(Color.Unspecified) }
    val action = remember { mutableStateOf({}) }

    LaunchedEffect(installState) {
        when (installState) {
            InstallState.Downloaded -> {
                progressMessage.value = context.getString(R.string.downloaded)
                icon.intValue = AppIcons.Done
                withDismissAction.value = true
                actionLabel.value = context.getString(R.string.update)
                action.value = onUpdateRequest
            }
            is InstallState.Downloading -> {
                val percentage = if (installState.totalBytesDownloaded > 0L) {
                    (installState.bytesDownloaded.toFloat() / installState.totalBytesDownloaded) * 100
                } else 0f
                progressMessage.value = context.getString(R.string.downloading, percentage.toInt())
                icon.intValue = AppIcons.Download
                iconTint.value = whiteTint
                withDismissAction.value = false
                actionLabel.value = null
                action.value = {}
            }
            is InstallState.Failed -> {
                progressMessage.value = installState.details.asString(context)
                iconTint.value = Color.Unspecified
                icon.intValue = AppIcons.WarningRed
                withDismissAction.value = true
                actionLabel.value = context.getString(R.string.retry_update)
                action.value = onRetryRequest
            }
            else -> {
                action.value = {}
            }
        }

        if (installState !is InstallState.Unknown && installState !is InstallState.NothingToInstall) {
            downloadingInit = true
            snackbarController.mutableShow(
                message = progressMessage,
                withDismissAction = withDismissAction,
                icon = icon,
                onAction = action.value,
                actionLabel = actionLabel,
                iconTint = iconTint
            )
        }
    }
}