package ru.bgitu.app.presentation.components

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import ru.bgitu.app.presentation.MainActivityUiState
import ru.bgitu.components.updates.api.model.InstallState
import ru.bgitu.components.updates.api.model.NativeUpdateInfo
import ru.bgitu.components.updates.api.model.UpdateAvailability
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.update.model.AppUpdateSheetData
import ru.bgitu.feature.update.presentation.AppUpdateBottomSheet

@Composable
fun AppUpdateBottomSheetSetup(uiState: MainActivityUiState) {
    var sheetData by remember { mutableStateOf<AppUpdateSheetData?>(null) }

    var showedUpdateAlert by rememberSaveable(
        saver = Saver(
            save = { it.value },
            restore = { mutableStateOf(it) }
        ),
        init = { mutableStateOf(false) }
    )

    LaunchedEffect(uiState, sheetData) {
        when (val info = uiState.updateInfo) {
            is NativeUpdateInfo -> {
                if (uiState.installState !is InstallState.Unknown
                    && uiState.installState !is InstallState.NothingToInstall) {
                    sheetData = null
                    showedUpdateAlert = true
                    return@LaunchedEffect
                }

                if (info.updateAvailability != UpdateAvailability.UPDATE_AVAILABLE) {
                    showedUpdateAlert = true
                    return@LaunchedEffect
                }

                val data = AppUpdateSheetData(
                    sizeBytes = info.totalBytesToDownload,
                    forced = info.forced,
                    availableVersionCode = info.availableVersionCode
                )

                if (info.forced && uiState.installState is InstallState.NothingToInstall) {
                    sheetData = data
                    return@LaunchedEffect
                }

                if (!showedUpdateAlert && !info.forced) {
                    showedUpdateAlert = true
                    sheetData = data
                }
            }
            else -> Unit
        }
    }

    sheetData?.let { data ->
        ModalBottomSheet(
            onDismissRequest = { sheetData = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = AppTheme.colorScheme.background1
        ) {
            AppUpdateBottomSheet(
                sheetData = data,
                onDismissRequest = { sheetData = null }
            )
        }
    }
}