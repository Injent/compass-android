package ru.bgitu.feature.settings.presentation.components

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppCheckBox
import ru.bgitu.core.designsystem.components.AppSwitch
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.model.settings.SubscribedTopic
import ru.bgitu.core.ui.Headline
import ru.bgitu.feature.settings.R
import ru.bgitu.feature.settings.presentation.settings.SettingsIntent
import ru.bgitu.feature.settings.presentation.settings.SettingsUiState
import kotlin.math.roundToInt

@Composable
internal fun NotificationsGroup(
    uiState: SettingsUiState.Success,
    onIntent: (SettingsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var notificationsEnabled by remember {
        var isGranted = true
        if (SDK_INT >= 33) {
            isGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PERMISSION_GRANTED
        }
        mutableStateOf(isGranted)
    }

    val notificationPermissionState = if (SDK_INT >= 33) {
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            notificationsEnabled = isGranted
        }
    } else null

    var showNotificationDialog by rememberSaveable { mutableStateOf(false) }

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
                    context.openSettings()
                }
            },
            onDismiss = {
                showNotificationDialog = false
            },
            text = stringResource(R.string.dialog_allowNotifications)
        )
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        if (showNotificationDialog) {
            showNotificationDialog = false
            onIntent(SettingsIntent.SetNotificationsEnabled(true))
        }

        if (SDK_INT >= 33) {
            notificationsEnabled = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PERMISSION_GRANTED
        }
    }

    Column(
        modifier = modifier
    ) {
        Headline(
            text = stringResource(R.string.group_notifications),
            modifier = Modifier.padding(bottom = AppTheme.spacing.mNudge)
        )

        Column(
            modifier = Modifier
                .background(AppTheme.colorScheme.background4, AppTheme.shapes.default)
                .border(
                    width = AppTheme.strokeWidth.thick,
                    color = animateColorAsState(
                        targetValue = if (uiState.prefs.notificationDelegationEnabled) {
                            AppTheme.colorScheme.background1
                        } else Color.Transparent
                    ).value,
                    shape = AppTheme.shapes.default
                )
                .animateContentSize()
        ) {
            fun onSwitch() {
                if (notificationsEnabled) {
                    onIntent(SettingsIntent.SetNotificationsEnabled(
                        !uiState.prefs.notificationDelegationEnabled))
                } else {
                    showNotificationDialog = true
                }
            }

            AppCard(
                onClick = { onSwitch() },
                modifier = Modifier.zIndex(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.notifications),
                        style = AppTheme.typography.headline2,
                        color = AppTheme.colorScheme.foreground1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = AppTheme.spacing.l)
                    )
                    AppSwitch(
                        checked = uiState.prefs.notificationDelegationEnabled && notificationsEnabled,
                        onCheckedChange = { onSwitch() },
                        modifier = Modifier
                    )
                }
            }

            AnimatedVisibility(
                visible = uiState.prefs.notificationDelegationEnabled && notificationsEnabled,
                enter = slideInVertically { (-it * 1.5).roundToInt() },
                exit = slideOutVertically { (-it * 1.5).roundToInt() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.spacing.l)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s)
                ) {
                    SubscribedTopic.entries.forEach { topic ->
                        AppCheckBox(
                            checked = topic in uiState.prefs.subscribedTopics,
                            onCheckedChange = {
                                onIntent(SettingsIntent.ChangeTopicSubscription(topic, it))
                            },
                            text = stringResource(
                                when (topic) {
                                    SubscribedTopic.SCHEDULE_CHANGE -> R.string.schedule_changes
                                    SubscribedTopic.BROADCAST -> R.string.broadcast
                                    SubscribedTopic.SEVERE -> R.string.severe_notifications
                                }
                            ),
                            enabled = topic != SubscribedTopic.SEVERE,
                            loading = topic in uiState.subscriptionsInProgress,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}