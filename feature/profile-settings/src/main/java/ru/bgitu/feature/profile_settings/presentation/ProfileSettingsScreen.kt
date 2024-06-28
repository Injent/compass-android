package ru.bgitu.feature.profile_settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppDialogBase
import ru.bgitu.core.designsystem.components.AppSwitch
import ru.bgitu.core.designsystem.components.AppTextField
import ru.bgitu.core.designsystem.components.DynamicAsyncImage
import ru.bgitu.core.designsystem.components.Status
import ru.bgitu.core.designsystem.components.StatusDecor
import ru.bgitu.core.designsystem.components.Tag
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.ClearFocusWithImeEffect
import ru.bgitu.core.designsystem.util.boxShadow
import ru.bgitu.core.model.UserProfile
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.ProfileImage
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.profile_settings.R

@Composable
fun ProfileSettingsScreen() {
    val navController = LocalNavController.current

    val screenModel: ProfileSettingsViewModel = koinViewModel()
    screenModel.events.listenEvents { event ->
        when (event) {
            ProfileSettingsEvent.Back -> navController.back()
        }
    }

    val profile by screenModel.profile.collectAsStateWithLifecycle()

    ProfileSettingsScreenContent(
        profile = profile,
        bioField = screenModel.bioField,
        nameField = screenModel.nameField,
        lastNameField = screenModel.lastNameField,
        onIntent = screenModel::onIntent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileSettingsScreenContent(
    profile: UserProfile,
    bioField: TextFieldState,
    nameField: TextFieldState,
    lastNameField: TextFieldState,
    onIntent: (ProfileSettingsIntent) -> Unit
) {
    ClearFocusWithImeEffect()

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
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            modifier = Modifier
                .padding(paddingValues)
                .padding(
                    top = AppTheme.spacing.l,
                    start = AppTheme.spacing.l,
                    end = AppTheme.spacing.l
                )
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                ProfileImage(
                    avatarUrl = profile.avatarUrl,
                    modifier = Modifier.matchParentSize()
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
            Status(
                statusDecor = StatusDecor.ADMIN,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            AppTextField(
                state = nameField,
                label = stringResource(R.string.first_name),
                modifier = Modifier.fillMaxWidth()
            )
            AppTextField(
                state = lastNameField,
                label = stringResource(R.string.last_name),
                modifier = Modifier.fillMaxWidth()
            )
            AppTextField(
                state = bioField,
                label = stringResource(R.string.bio),
                modifier = Modifier.fillMaxWidth()
            )
            profile.contacts?.let {

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.public_profile),
                    style = AppTheme.typography.body,
                    color = AppTheme.colorScheme.foreground2
                )
                AppSwitch(
                    checked = profile.publicProfile,
                    onCheckedChange = { onIntent(ProfileSettingsIntent.SwitchVisibility) }
                )
            }
        }
    }
}

@Composable
private fun AddLinkDialog(
    state: TextFieldState,
    modifier: Modifier = Modifier
) {
    AppDialogBase(
        modifier = modifier,
        title = stringResource(R.string.add_link),
        onConfirm = { /*TODO*/ },
        onDismiss = {  }
    ) {
        AppTextField(
            state = state,
            label = "URL",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AvatarBlock(
    avatarUrl: String?,
    fullName: String,
    role: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        DynamicAsyncImage(
            imageUrl = avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.height(AppTheme.spacing.l))
        Text(
            text = fullName,
            style = AppTheme.typography.headline1,
            color = AppTheme.colorScheme.foreground1,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(AppTheme.spacing.l))
        Tag(text = role, hasContainer = true, icon = AppIcons.Approved)
    }
}