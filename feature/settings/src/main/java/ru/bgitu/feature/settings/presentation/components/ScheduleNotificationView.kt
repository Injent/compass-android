package ru.bgitu.feature.settings.presentation.components

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import ru.bgitu.core.designsystem.components.AppSwitch
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.ui.Headline
import ru.bgitu.feature.settings.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScheduleNotificationView(
    enabled: Boolean,
    onSwitch: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val canScheduleAlarms = remember {
        if (SDK_INT >= 31) {
            context.getSystemService<AlarmManager>()!!.canScheduleExactAlarms()
        } else {
            true
        }
    }
    var showAlarmDialog by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }

    val notificationPermissionState = if (SDK_INT >= 33) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS) {
            if (canScheduleAlarms) {
                onSwitch(!enabled)
            } else {
                showAlarmDialog = true
            }
        }
    } else null

    if (showNotificationDialog) {
        NotificationsPermissionDialog(
            onConfirm = {
                if (notificationPermissionState?.status?.shouldShowRationale == false) {
                    notificationPermissionState.launchPermissionRequest()
                } else {
                    context.openSettings()
                }

                showNotificationDialog = false
            },
            onDismiss = { showNotificationDialog = false }
        )
    }

    var alarmButtonClicked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(alarmButtonClicked) {
        if (alarmButtonClicked) {
            onSwitch(!enabled)
        }
    }

    if (showAlarmDialog) {
        ScheduleAlarmDialog(
            onConfirm = {
                alarmButtonClicked = true
                if (SDK_INT >= 31) {
                    context.openScheduleAlarmSettings()
                }
            },
            onDismiss = { showAlarmDialog = false }
        )
    }

    ScheduleNotificationContent(
        enabled = enabled,
        onSwitch = onSwitch,
        canShowNotifications = notificationPermissionState?.status?.isGranted ?: true,
        canScheduleAlarm = canScheduleAlarms,
        showNotificationDialog = { showNotificationDialog = true },
        showScheduleAlarmDialog = { showAlarmDialog = true },
        modifier = modifier
    )
}

@Composable
private fun ScheduleNotificationContent(
    enabled: Boolean,
    canShowNotifications: Boolean,
    canScheduleAlarm: Boolean,
    onSwitch: (Boolean) -> Unit,
    showScheduleAlarmDialog: () -> Unit,
    showNotificationDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.mNudge),
        modifier = modifier
    ) {
        Headline(text = stringResource(R.string.schedule_in_notification))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.show_schedule),
                style = AppTheme.typography.headline1,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier.padding(end = AppTheme.spacing.l)
            )
            AppSwitch(
                checked = true,
                onCheckedChange = {
                    if (!canShowNotifications) {
                        showNotificationDialog()
                        return@AppSwitch
                    }

                    if (canScheduleAlarm) {
                        onSwitch(!enabled)
                    } else {
                        showScheduleAlarmDialog()
                    }
                },
                modifier = modifier
            )
        }
        var showDetails by remember { mutableStateOf(false) }
        val detailsArrowRotation by animateFloatAsState(
            targetValue = if (showDetails) 90f else 0f
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xxs),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.spacing.xxs)
                .clickable { showDetails = !showDetails }
        ) {
            Text(
                text = stringResource(R.string.more_detailed),
                style = AppTheme.typography.calloutButton,
                color = AppTheme.colorScheme.foreground3
            )
            Icon(
                painter = painterResource(AppIcons.SmallArrowNext),
                contentDescription = null,
                tint = AppTheme.colorScheme.foreground3,
                modifier = Modifier
                    .size(12.dp)
                    .rotate(detailsArrowRotation)
            )
        }

        AnimatedVisibility(
            visible = showDetails,
            modifier = Modifier.clickable { showDetails = !showDetails }
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.show_schedule_details))
                },
                style = AppTheme.typography.footnote,
                color = AppTheme.colorScheme.foreground2
            )
        }
    }

}

@RequiresApi(Build.VERSION_CODES.S)
private fun Context.openScheduleAlarmSettings() {
    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).also(::startActivity)
}

private fun Context.openSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}