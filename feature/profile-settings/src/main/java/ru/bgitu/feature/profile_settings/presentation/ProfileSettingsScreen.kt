package ru.bgitu.feature.profile_settings.presentation

import android.webkit.URLUtil
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppDialogBase
import ru.bgitu.core.designsystem.components.AppReadOnlyTextField
import ru.bgitu.core.designsystem.components.AppSwitch
import ru.bgitu.core.designsystem.components.AppSwitchTokens
import ru.bgitu.core.designsystem.components.DynamicAsyncImage
import ru.bgitu.core.designsystem.components.Status
import ru.bgitu.core.designsystem.components.StatusDecor
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.ScrollBarConfig
import ru.bgitu.core.designsystem.util.boxShadow
import ru.bgitu.core.designsystem.util.horizontalScrollWithScrollbar
import ru.bgitu.core.model.UserProfile
import ru.bgitu.core.navigation.BackResultEffect
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.ProfileImage
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.core.ui.onClick
import ru.bgitu.feature.input.navigation.InputParams
import ru.bgitu.feature.input.navigation.navigateToInput
import ru.bgitu.feature.profile_settings.R

private const val RESULT_DISPLAY_NAME = "result_display_name"
private const val RESULT_BIO = "result_bio"
private const val MAX_BIO_LENGHT = 300
private const val MAX_NAME_LENGHT = 24
private const val MAX_BIO_LINES = 6

@Composable
fun ProfileSettingsRoute() {
    val navController = LocalNavController.current

    val viewModel: ProfileSettingsViewModel = koinViewModel()
    viewModel.events.listenEvents { event ->
        when (event) {
            ProfileSettingsEvent.Back -> navController.back()
        }
    }

    BackResultEffect {
        onResult<String>(RESULT_DISPLAY_NAME) { name ->
            viewModel.onIntent(ProfileSettingsIntent.SetDisplayName(name))
        }
        onResult<String>(RESULT_BIO) { bio ->
            viewModel.onIntent(ProfileSettingsIntent.SetBio(bio))
        }
    }

    val profile by viewModel.profile.collectAsStateWithLifecycle()

    ProfileSettingsScreen(
        profile = profile,
        onIntent = viewModel::onIntent,
        onInputRequest = navController::navigateToInput
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileSettingsScreen(
    profile: UserProfile,
    onIntent: (ProfileSettingsIntent) -> Unit,
    onInputRequest: (InputParams) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_settings),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1
                    )
                },
                navigationIcon = {
                    AppBackButton(onClick = { onIntent(ProfileSettingsIntent.Back) })
                },
                modifier = Modifier.boxShadow()
            )
        },
        modifier = Modifier.statusBarsPadding()
    ) { paddingValues ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            modifier = Modifier
                .padding(paddingValues)
                .padding(
                    top = AppTheme.spacing.l,
                    start = AppTheme.spacing.l,
                    end = AppTheme.spacing.l
                )
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PickableProfileImage(
                        avatarUrl = profile.avatarUrl,
                        onChangeAvatar = { avatarUrl ->
                            onIntent(ProfileSettingsIntent.SetProfileImage(avatarUrl))
                        },
                    )

                    Status(statusDecor = StatusDecor.ADMIN)
                }
            }

            item {
                AppReadOnlyTextField(
                    text = profile.displayName,
                    label = stringResource(R.string.display_name),
                    placeholder = stringResource(R.string.enter_display_name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onClick {
                            onInputRequest(
                                InputParams(
                                    title = context.getString(R.string.display_name),
                                    description = context.getString(R.string.display_name),
                                    placeholder = context.getString(R.string.enter_display_name),
                                    resultKey = RESULT_DISPLAY_NAME,
                                    initialText = profile.displayName,
                                    inputTransformation = {
                                        if (length > MAX_NAME_LENGHT) {
                                            revertAllChanges()
                                        }
                                    }
                                )
                            )
                        }
                )
            }

            item {
                AppReadOnlyTextField(
                    text = profile.bio,
                    label = stringResource(R.string.bio),
                    placeholder = stringResource(R.string.hint_bio),
                    maxLines = 6,
                    fieldMinHeight = 96.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onClick {
                            onInputRequest(
                                InputParams(
                                    title = context.getString(R.string.bio),
                                    description = context.getString(R.string.bio),
                                    placeholder = context.getString(R.string.hint_bio),
                                    resultKey = RESULT_BIO,
                                    initialText = profile.bio,
                                    maxLines = MAX_BIO_LINES,
                                )
                            )
                        }
                )
            }

            item {
                AppCard(
                    onClick = {
                        onIntent(ProfileSettingsIntent.SetPublicProfile(!profile.publicProfile))
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.public_profile),
                            style = AppTheme.typography.body,
                            color = AppTheme.colorScheme.foreground1
                        )
                        AppSwitch(
                            checked = profile.publicProfile,
                            onCheckedChange = { checked ->
                                onIntent(ProfileSettingsIntent.SetPublicProfile(checked))
                            }
                        )
                    }
                    Text(
                        text = stringResource(R.string.public_profile_description),
                        style = AppTheme.typography.callout,
                        color = AppTheme.colorScheme.foreground2,
                        modifier = Modifier
                            .padding(
                                top = AppTheme.spacing.xs,
                                end = AppSwitchTokens.TrackWidth + AppTheme.spacing.m,
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun PickableProfileImage(
    avatarUrl: String?,
    onChangeAvatar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var dialogOpen by rememberSaveable { mutableStateOf(false) }

    if (dialogOpen) {
        ProfileImageInputDialog(
            onChangeAvatar = {
                dialogOpen = false
                onChangeAvatar(it)
            },
            onDismiss = {
                dialogOpen = false
            }
        )
    }

    Box(
        modifier = modifier
            .size(80.dp)
            .onClick {
                dialogOpen = true
            }
    ) {
        ProfileImage(
            avatarUrl = avatarUrl,
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
        )
        Icon(
            painter = painterResource(AppIcons.Edit),
            contentDescription = null,
            tint = AppTheme.colorScheme.foregroundOnBrand,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.BottomEnd)
                .background(
                    color = AppTheme.colorScheme.foreground,
                    shape = CircleShape
                )
                .padding(5.dp)
        )
    }
}

@Composable
private fun ProfileImageInputDialog(
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

    AppDialogBase(
        title = stringResource(R.string.profile_photo),
        onConfirm = {
            if (isError) {
                onDismiss()
            } else {
                onChangeAvatar(profileUrl)
            }
        },
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