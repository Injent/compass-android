package ru.bgitu.app.presentation.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import ru.bgitu.app.R
import ru.bgitu.feature.settings.presentation.components.NotificationsPermissionDialog

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NotificationDialogEffect() {
    val context = LocalContext.current

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var dialogDismissed by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showDialog = false
        }
        dialogDismissed = !isGranted
    }


    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED) {
            return@LaunchedEffect
        }
        val shouldShowRationale = (context as ComponentActivity).shouldShowRequestPermissionRationale(
            Manifest.permission.POST_NOTIFICATIONS
        )

        showDialog = !shouldShowRationale
    }

    if (showDialog) {
        NotificationsPermissionDialog(
            onConfirm = {
                if (dialogDismissed) {
                    showDialog = false
                    return@NotificationsPermissionDialog
                }
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            onDismiss = { showDialog = false },
            text = stringResource(
                if (dialogDismissed) {
                    R.string.dialog_youCanEnableNotifications
                } else R.string.dialog_wantToReceiveNotifications
            )
        )
    }
}