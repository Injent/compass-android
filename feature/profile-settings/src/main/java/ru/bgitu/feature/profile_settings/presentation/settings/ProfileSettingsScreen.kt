package ru.bgitu.feature.profile_settings.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppReadOnlyTextField
import ru.bgitu.core.designsystem.components.Status
import ru.bgitu.core.designsystem.components.StatusDecor
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Edit
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.model.UserProfile
import ru.bgitu.core.navigation.BackResultEffect
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.back
import ru.bgitu.core.navigation.push
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.ProfileImage
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.core.ui.onClick
import ru.bgitu.feature.input.navigation.InputParams
import ru.bgitu.feature.input.navigation.navigateToInput
import ru.bgitu.feature.profile_settings.R
import ru.bgitu.feature.profile_settings.presentation.components.ContactOption
import ru.bgitu.feature.profile_settings.presentation.components.ExpertOption
import ru.bgitu.feature.profile_settings.presentation.components.ProfileImageInputDialog
import ru.bgitu.feature.profile_settings.presentation.components.PublicProfileOption
import ru.bgitu.feature.profile_settings.presentation.components.VariantsOption

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
            ProfileSettingsEvent.NavigateToExpertApply -> navController.push(Screen.ExpertApply)
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
        contentWindowInsets = WindowInsets(bottom = 0),
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
                    AppBackButton(onClick = { ProfileSettingsIntent.Back.also(onIntent) })
                },
                modifier = Modifier
            )
        },
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            contentPadding = PaddingValues(
                top = AppTheme.spacing.l,
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + AppTheme.spacing.l
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppTheme.spacing.l)
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PickableProfileImage(
                        avatarUrl = profile.avatarUrl,
                        onChangeAvatar = { avatarUrl ->
                            ProfileSettingsIntent.SetProfileImage(avatarUrl).also(onIntent)
                        },
                    )

                    Status(statusDecor = StatusDecor.valueOf(profile.userRole.name))
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
                    label = stringResource(R.string.about_me),
                    placeholder = stringResource(R.string.hint_bio),
                    maxLines = 6,
                    fieldMinHeight = 96.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onClick {
                            onInputRequest(
                                InputParams(
                                    title = context.getString(R.string.about_me),
                                    description = context.getString(R.string.about_me),
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
                VariantsOption(
                    variants = profile.variants,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                ExpertOption(
                    onClick = {
                        ProfileSettingsIntent.NavigateToExpertApply.also(onIntent)
                    }
                )
            }

            profile.contacts?.let {
                item {
                    ContactOption(
                        contacts = it
                    )
                }
            }

            item {
                PublicProfileOption(
                    enabled = profile.publicProfile,
                    onSwitch = { enabled ->
                        ProfileSettingsIntent.SetPublicProfile(enabled).also(onIntent)
                    }
                )
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
            imageVector = AppIcons.Edit,
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