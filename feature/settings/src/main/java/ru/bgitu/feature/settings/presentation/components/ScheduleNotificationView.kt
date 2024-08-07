package ru.bgitu.feature.settings.presentation.components

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppCardTokens
import ru.bgitu.core.designsystem.components.AppSwitch
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.ui.Headline
import ru.bgitu.feature.settings.R
import ru.bgitu.feature.settings.presentation.settings.SettingsUiState

enum class RequiredFeature {
    GROUP,
    POST_NOTIFICATIONS,
    SCHEDULE_ALARM
}

@Composable
fun ScheduleNotificationView(
    uiState: SettingsUiState.Success,
    onGroupSelectRequest: () -> Unit,
    onSwitch: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val alarmManager = remember { context.getSystemService<AlarmManager>()!! }
    val requiredFeatures = remember(uiState.isGroupSelected) {
        val requiredFeatures = mutableListOf<RequiredFeature>()

        if (SDK_INT >= 33 && context.checkSelfPermission(
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_DENIED) {
            requiredFeatures += RequiredFeature.POST_NOTIFICATIONS
        }
        if (SDK_INT >= 31 && !alarmManager.canScheduleExactAlarms()) {
            requiredFeatures += RequiredFeature.SCHEDULE_ALARM
        }
        if (!uiState.isGroupSelected) {
            requiredFeatures += RequiredFeature.GROUP
        }

        mutableStateListOf(*requiredFeatures.toTypedArray())
    }

    var showGroupDialog by remember { mutableStateOf(false) }
    var showAlarmDialog by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }
    var rememberedPermissionClick by remember { mutableStateOf(false) }

    fun onSwitchOrRequestFeature() {
        when {
            requiredFeatures.isEmpty() -> onSwitch(!uiState.prefs.showPinnedSchedule) // should stay in first pos to prevent over calculations
            RequiredFeature.POST_NOTIFICATIONS in requiredFeatures -> showNotificationDialog = true
            RequiredFeature.SCHEDULE_ALARM in requiredFeatures -> showAlarmDialog = true
            RequiredFeature.GROUP in requiredFeatures -> showGroupDialog = true
            else -> onSwitch(!uiState.prefs.showPinnedSchedule)
        }
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        if (SDK_INT >= 31 && alarmManager.canScheduleExactAlarms()) {
            requiredFeatures -= RequiredFeature.SCHEDULE_ALARM
        }
        if (SDK_INT >= 33 && context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED) {
            requiredFeatures -= RequiredFeature.POST_NOTIFICATIONS
        }
        if (rememberedPermissionClick) {
            onSwitchOrRequestFeature()
            rememberedPermissionClick = false
        }
    }

    val notificationPermissionState = if (SDK_INT >= 33) {
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                requiredFeatures -= RequiredFeature.POST_NOTIFICATIONS
                onSwitchOrRequestFeature()
            }
        }
    } else null

    if (showNotificationDialog) {
        NotificationsPermissionDialog(
            onConfirm = {
                val shouldShowRationale = if (SDK_INT >= 33) {
                    (context as ComponentActivity).shouldShowRequestPermissionRationale(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                } else return@NotificationsPermissionDialog

                if (!shouldShowRationale) {
                    notificationPermissionState?.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    rememberedPermissionClick = true
                    context.openSettings()
                }
                showNotificationDialog = false
            },
            onDismiss = { showNotificationDialog = false }
        )
    }

    if (showAlarmDialog) {
        ScheduleAlarmDialog(
            onConfirm = {
                if (SDK_INT >= 31) {
                    rememberedPermissionClick = true
                    context.openScheduleAlarmSettings()
                }
                showAlarmDialog = false
            },
            onDismiss = { showAlarmDialog = false }
        )
    }

    if (showGroupDialog) {
        SelectGroupDialog(
            onConfirm = {
                onGroupSelectRequest()
                showGroupDialog = false
            },
            onDismiss = { showGroupDialog = false }
        )
    }

    ScheduleNotificationContent(
        enabled = uiState.prefs.showPinnedSchedule,
        plannedTime = uiState.nextScheduleNotification,
        onSwitchRequest = {
            // No need to request permissions to turn off option
            if (uiState.prefs.showPinnedSchedule) {
                onSwitch(false)
            } else onSwitchOrRequestFeature()
        },
        modifier = modifier
    )
}

@Composable
private fun ScheduleNotificationContent(
    enabled: Boolean,
    plannedTime: LocalDateTime?,
    onSwitchRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Headline(
            text = stringResource(R.string.schedule_in_notification),
            modifier = Modifier.padding(bottom = AppTheme.spacing.mNudge)
        )
        AppCard(
            contentPadding = PaddingValues(),
            onClick = onSwitchRequest
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppCardTokens.ContentPadding)
            ) {
                Text(
                    text = stringResource(R.string.show_schedule),
                    style = AppTheme.typography.headline2,
                    color = AppTheme.colorScheme.foreground1,
                    modifier = Modifier.padding(end = AppTheme.spacing.l)
                )
                AppSwitch(
                    checked = enabled,
                    onCheckedChange = { onSwitchRequest() },
                    modifier = modifier
                )
            }

            AnimatedVisibility(
                visible = plannedTime != null,
                enter = expandVertically(
                    animationSpec = tween()
                ) + fadeIn(
                    animationSpec = tween(delayMillis = 300)
                ),
                exit = fadeOut(
                    animationSpec = tween()
                ) + shrinkVertically(
                    animationSpec = tween(delayMillis = 300)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                var formatedTime = remember {
                    plannedTime?.let { DateTimeUtil.formatTime(plannedTime.time) }
                }

                LaunchedEffect(plannedTime) {
                    if (plannedTime == null) {
                        delay(300)
                        formatedTime = null
                    }
                }

                formatedTime?.let { time ->
                    Surface(
                        shape = RectangleShape,
                        color = AppTheme.colorScheme.backgroundBrand,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(AppTheme.shapes.defaultBottomCarved)
                    ) {
                        Text(
                            text = stringResource(R.string.scheduled_at, time),
                            style = AppTheme.typography.callout,
                            color = AppTheme.colorScheme.foregroundOnBrand,
                            modifier = Modifier.padding(
                                horizontal = AppTheme.spacing.xl,
                                vertical = AppTheme.spacing.xs
                            )
                        )
                    }
                }
            }
        }
        var showDetails by remember { mutableStateOf(false) }
        val detailsArrowRotation by animateFloatAsState(
            targetValue = if (showDetails) 90f else -90f
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.spacing.s)
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