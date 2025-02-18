package ru.bgitu.feature.profile.presentation

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.os.Build.VERSION.SDK_INT
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.bgitu.core.common.ScreenRotation
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.common.screenRotation
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.datastore.model.UnseenFeatures
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppItemCard
import ru.bgitu.core.designsystem.components.AppSmallButton
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.components.MotionContent
import ru.bgitu.core.designsystem.components.Status
import ru.bgitu.core.designsystem.components.StatusDecor
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.CalendarOutlined
import ru.bgitu.core.designsystem.icon.Edit
import ru.bgitu.core.designsystem.icon.Vk
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.designsystem.theme.bottom
import ru.bgitu.core.designsystem.theme.start
import ru.bgitu.core.designsystem.util.boxShadow
import ru.bgitu.core.designsystem.util.shimmer
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.push
import ru.bgitu.core.ui.Headline
import ru.bgitu.core.ui.ProfileImage
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.core.ui.onClick
import ru.bgitu.feature.profile.R
import ru.bgitu.feature.profile.model.ProfileItem
import ru.bgitu.feature.profile.presentation.components.SignOutDialog
import ru.bgitu.feature.profile.presentation.components.SupportDevelopersCard
import ru.bgitu.feature.profile.presentation.components.TryNewFeatureCard
import ru.bgitu.feature.profile.presentation.components.TryNewFeatureDialog
import ru.bgitu.feature.schedule_widget.presentation.WidgetSettingsActivity
import ru.bgitu.feature.schedule_widget.receiver.ScheduleWidgetReceiver
import kotlin.math.roundToInt

@Composable
internal fun ProfileScreen() {
    val navController = LocalNavController.current
    val context = LocalContext.current

    val viewModel: ProfileViewModel = koinViewModel(viewModelStoreOwner = context as ComponentActivity)

    viewModel.events.listenEvents { event ->
        when (event) {
            is ProfileEvent.NavigateToLogin -> {
                if (event.autoSignOut)
                    navController.navigate(Screen.Login()) {
                        popUpTo<Screen.MainGraph> {
                            inclusive = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                else
                    navController.push(Screen.Login(compactScreen = true))
            }
            ProfileEvent.NavigateToSettings -> navController.push(Screen.Settings)
            ProfileEvent.NavigateToProfileSettings -> navController.push(Screen.ProfileSettings)
            ProfileEvent.NavigateToAboutApp -> navController.push(Screen.About)
            ProfileEvent.NavigateToHelp -> navController.push(Screen.Help)
            ProfileEvent.NavigateToGroups -> navController.push(Screen.Groups)
        }
    }

    val uiState by viewModel.profile.collectAsStateWithLifecycle()

    NewProfileScreenContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun ProfileMenuItems(
    onIntent: (ProfileIntent) -> Unit,
    hideSettings: Boolean,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val settings = koinInject<SettingsRepository>()
    val unseenFeatures by settings.unseenFeatures.collectAsStateWithLifecycle(emptyList())

    ProfileItem.entries.forEach { profileItem ->
        if (hideSettings && profileItem == ProfileItem.SETTINGS) return@forEach

        Box {
            AppItemCard(
                label = stringResource(profileItem.labelRes),
                icon = profileItem.icon,
                onClick = {
                    when (profileItem) {
                        ProfileItem.HELP -> ProfileIntent.NavigateToHelp
                        ProfileItem.ABOUT -> ProfileIntent.NavigateToAboutApp
                        ProfileItem.MY_GROUPS -> ProfileIntent.NavigateToGroups
                        ProfileItem.SETTINGS -> ProfileIntent.NavigateToSettings
                    }.also(onIntent)
                }
            )

            if (UnseenFeatures.NEW_CHANGELOG in unseenFeatures && profileItem == ProfileItem.ABOUT) {
                Badge(
                    containerColor = AppTheme.colorScheme.foregroundError,
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }

    val appWidgetManager = remember { AppWidgetManager.getInstance(context) }

    val showWidgetCard = remember {
        if (SDK_INT >= 26) {
            appWidgetManager.isRequestPinAppWidgetSupported
        } else false
    }
    val isWidgetPlaced = remember {
        appWidgetManager.getAppWidgetIds(ComponentName(context, ScheduleWidgetReceiver::class.java))
            .isNotEmpty()
    }

    if (showWidgetCard) {
        Box {
            AppItemCard(
                label = remember(unseenFeatures, isWidgetPlaced) {
                    context.getString(
                        when {
                            UnseenFeatures.ADD_WIDGET in unseenFeatures -> R.string.try_widget
                            isWidgetPlaced -> R.string.widget_settings
                            else -> R.string.add_widget
                        }
                    )
                },
                icon = AppIcons.CalendarOutlined,
                onClick = {
                    val provider = ComponentName(context, ScheduleWidgetReceiver::class.java)

                    if (isWidgetPlaced) {
                        val widgetId = appWidgetManager.getAppWidgetIds(provider).first()
                        val intent = Intent(context, WidgetSettingsActivity::class.java).apply {
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                            flags = FLAG_ACTIVITY_NO_ANIMATION
                        }
                        context.startActivity(intent)
                        return@AppItemCard
                    }

                    scope.launch(NonCancellable) {
                        settings.updateMetadata {
                            it.copy(unseenFeatureIds = it.unseenFeatureIds - UnseenFeatures.ADD_WIDGET)
                        }
                    }


                    if (SDK_INT >= 26) {
                        appWidgetManager.requestPinAppWidget(provider, null, null)
                    }
                }
            )
            if (UnseenFeatures.ADD_WIDGET in unseenFeatures) {
                Badge(
                    containerColor = AppTheme.colorScheme.foregroundError,
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
private fun ProfileScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    avatarUrl: String?
) {
    val additionalStatusBarHeight = LocalDensity.current.run {
        WindowInsets.statusBars.getTop(this).toDp()
    }
    MotionContent(
        scrollBehavior = scrollBehavior,
        motionSceneResId = R.raw.profile_topbar_scene,
        params = mapOf(
            "title_top" to additionalStatusBarHeight.value.roundToInt()
        ),
        pinnedHeight = 56.dp + additionalStatusBarHeight,
        maxHeight = 120.dp + additionalStatusBarHeight,
        modifier = Modifier
    ) { fraction ->
        val headerColorTransition = AppTheme.colorScheme.background3.copy(fraction)
        Box(
            modifier = Modifier
                .layoutId("background")
                .fillMaxSize()
                .boxShadow(
                    alpha = fraction,
                    spreadRadius = 0.dp
                )
                .drawWithContent {
                    drawContent()
                    if (fraction > 0) {
                        drawRect(
                            color = headerColorTransition
                        )
                    }
                }
        ) {
            val gradientStartColor = AppTheme.colorScheme.background2
            val gradientBrush = remember(gradientStartColor) {
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        gradientStartColor,
                    )
                )
            }

            Image(
                painter = painterResource(R.drawable.profile_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .drawWithContent {
                        if (fraction < 1f) {
                            drawContent()
                            drawRect(brush = gradientBrush)
                        }
                    }
            )
        }

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .layoutId("title")
                .height(56.dp)
        ) {
            Text(
                text = stringResource(R.string.profile),
                style = AppTheme.typography.title3,
                color = AppTheme.colorScheme.foreground1,
            )
        }

        ProfileImage(
            avatarUrl = avatarUrl,
            modifier = Modifier
                .layoutId("avatar")
                .clip(CircleShape)
        )
    }
}

@Composable
private fun NewProfileScreenContent(
    uiState: ProfileUiState,
    onIntent: (ProfileIntent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(start = LocalExternalPadding.current.start),
        topBar = {
            ProfileScreenTopBar(
                scrollBehavior = scrollBehavior,
                avatarUrl = (uiState as? ProfileUiState.Success)?.profile?.avatarUrl
            )
        }
    ) { paddingValues ->
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
                modifier = Modifier
                    .fillMaxSize()
                    .thenIf(context.screenRotation == ScreenRotation.RIGHT) {
                        displayCutoutPadding()
                    }
                    .verticalScroll(
                        state = rememberScrollState(),
                    )
                    .padding(paddingValues)
                    .padding(AppTheme.spacing.l)
            ) {
                when (uiState) {
                    is ProfileUiState.Success -> {
                        Status(
                            statusDecor = StatusDecor.valueOf(uiState.profile.userRole.name),
                            modifier = Modifier.align(Alignment.End)
                        )
                        AuthorizedProfileHeader(
                            uiState = uiState,
                            onIntent = onIntent,
                            modifier = Modifier.fillMaxWidth()
                        )
                        AboutMeCard(
                            uiState = uiState,
                            onSpecifyRequest = {},
                            modifier = Modifier.fillMaxWidth()
                        )
                        ContactsCard(
                            uiState = uiState,
                            onSpecifyRequest = {},
                            modifier = Modifier.fillMaxWidth()
                        )
                        VariantsCard(
                            uiState = uiState,
                            onSpecifyRequest = { /*TODO*/ },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    else -> Status(
                        statusDecor = StatusDecor.REGULAR,
                        modifier = Modifier.align(Alignment.End)
                    )
                }

                AnimatedVisibility(
                    visible = (uiState as? ProfileUiState.Empty)?.shouldShowMateBanner ?: false,
                    enter = expandVertically(
                        animationSpec = tween(durationMillis = 400)
                    ) + fadeIn(
                        animationSpec = tween(delayMillis = 200)
                    ),
                    exit = shrinkVertically(
                        animationSpec = tween(delayMillis = 200)
                    ) + fadeOut(
                        animationSpec = tween()
                    )
                ) {
                    var showNewFeatureDialog by rememberSaveable { mutableStateOf(false) }

                    if (showNewFeatureDialog) {
                        TryNewFeatureDialog(
                            onConfirm = { onIntent(ProfileIntent.SignOut) },
                            onDismissRequest = { showNewFeatureDialog = false }
                        )
                    }

                    TryNewFeatureCard(
                        onClose = { onIntent(ProfileIntent.CloseMateBanner) },
                        onClick = { showNewFeatureDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                ProfileMenuItems(
                    onIntent = onIntent,
                    hideSettings = uiState is ProfileUiState.Success,
                )
                var showSignOutDialog by rememberSaveable { mutableStateOf(false) }

                if (showSignOutDialog) {
                    SignOutDialog(
                        onConfirm = { onIntent(ProfileIntent.SignOut) },
                        onDismiss = { showSignOutDialog = false }
                    )
                }

                SupportDevelopersCard(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(LocalExternalPadding.current.bottom))


                // TODO("Add signout button in future releases"
//                AppCard(
//                    onClick = { showSignOutDialog = true }
//                ) {
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(12.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            painter = painterResource(AppIcons.Logout),
//                            contentDescription = null,
//                            tint = AppTheme.colorScheme.foregroundError,
//                            modifier = Modifier
//                                .sizeIn(maxWidth = 20.dp, maxHeight = 20.dp)
//                        )
//
//                        Text(
//                            text = stringResource(R.string.sign_out),
//                            style = AppTheme.typography.headline1,
//                            color = AppTheme.colorScheme.foreground1,
//                            modifier = Modifier.weight(1f)
//                        )
//                    }
//                }

        }
    }
}

@Composable
private fun AuthorizedProfileHeader(
    uiState: ProfileUiState.Success,
    onIntent: (ProfileIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            modifier = Modifier
        ) {
            Text(
                text = uiState.profile.displayName,
                style = AppTheme.typography.headline1,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AppSmallButton(
                    text = stringResource(R.string.edit),
                    onClick = { onIntent(ProfileIntent.NavigateToProfileSettings) },
                    icon = AppIcons.Edit,
                    modifier = Modifier
                        .weight(1f)
                )
//                AppSmallButton(
//                    text = stringResource(R.string.settings),
//                    onClick = { onIntent(ProfileIntent.NavigateToSettings) },
//                    icon = AppIcons,
//                    modifier = Modifier
//                        .weight(1f)
//                )
            }
        }
    }
}

@Composable
private fun AboutMeCard(
    uiState: ProfileUiState,
    onSpecifyRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        modifier = modifier,
    ) {
        Headline(
            text = stringResource(R.string.about_me),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        when (uiState) {
            is ProfileUiState.Empty -> SpecifyCardContent(onClick = onSpecifyRequest)
            is ProfileUiState.Error -> Unit
            ProfileUiState.Loading -> ShimmerLoading()
            is ProfileUiState.Success -> {
                if (uiState.profile.bio.isBlank()) {
                    SpecifyCardContent(onClick = onSpecifyRequest)
                } else {
                    Text(
                        text = uiState.profile.bio,
                        style = AppTheme.typography.body,
                        color = AppTheme.colorScheme.foreground1
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactsCard(
    uiState: ProfileUiState,
    onSpecifyRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier) {
        Headline(
            text = stringResource(R.string.contacts),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        when (uiState) {
            is ProfileUiState.Empty -> SpecifyCardContent(onClick = onSpecifyRequest)
            is ProfileUiState.Error -> Unit
            ProfileUiState.Loading -> ShimmerLoading()
            is ProfileUiState.Success -> {
                uiState.profile.contacts.let { contacts ->
                    if (contacts == null) {
                        SpecifyCardContent(onClick = onSpecifyRequest)
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            contacts.vk?.let { url ->
                                LinkButton(
                                    icon = AppIcons.Vk,
                                    text = url.substringAfterLast('/'),
                                    url = url
                                )
                            }
                            contacts.vk?.let { url ->
                                LinkButton(
                                    icon = AppIcons.Vk,
                                    text = url.substringAfterLast('/'),
                                    url = url
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VariantsCard(
    uiState: ProfileUiState,
    onSpecifyRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Headline(
            text = stringResource(R.string.variants),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        when (uiState) {
            is ProfileUiState.Empty -> SpecifyCardContent(onClick = onSpecifyRequest)
            is ProfileUiState.Error -> Unit
            ProfileUiState.Loading -> ShimmerLoading()
            is ProfileUiState.Success -> {
                if (uiState.profile.variants.isEmpty()) {
                    SpecifyCardContent(onClick = onSpecifyRequest)
                }
                for (variantEntry in uiState.profile.variants) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = variantEntry.subjectName,
                            style = AppTheme.typography.body,
                            color = AppTheme.colorScheme.foreground1
                        )
                        HorizontalDivider(
                            thickness = AppTheme.strokeWidth.thin,
                            color = AppTheme.colorScheme.stroke2,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                        )
                        Text(
                            text = variantEntry.variant.toString(),
                            style = AppTheme.typography.body,
                            color = AppTheme.colorScheme.foreground1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShimmerLoading() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .height(24.dp)
            .shimmer(shape = AppTheme.shapes.small)
    )
}

@Composable
private fun SpecifyCardContent(
    onClick: () -> Unit,
) {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.2.dp,
        color = AppTheme.colorScheme.stroke1
    )
    Spacer(Modifier.height(6.dp))
    AppTextButton(
        text = stringResource(R.string.option_specify),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun LinkButton(
    icon: ImageVector,
    text: String,
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.onClick {
            context.openUrl(url)
        }
    ) {
        Image(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = AppTheme.typography.bodyButton,
            color = AppTheme.colorScheme.foreground1
        )
    }
}