package ru.bgitu.app.presentation.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.bgitu.app.BuildConfig
import ru.bgitu.app.navigation.topLevelRoutes
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.designsystem.components.TabItem
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Folder
import ru.bgitu.core.designsystem.icon.FolderOpen
import ru.bgitu.core.designsystem.icon.HomeInFolder
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.nonScaledSp
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.navigateTopDestination
import ru.bgitu.core.ui.ProfileImage

@Composable
fun DefaultIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = modifier.size(20.dp)
    )
}

@Composable
fun CalendarIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(modifier.size(AppTheme.spacing.xl)) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
        val monthNumber = remember {
            DateTimeUtil.currentDate.dayOfMonth.toString()
        }
        Text(
            text = monthNumber,
            style = TextStyle(
                fontSize = 12.sp.nonScaledSp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                color = AppTheme.colorScheme.background1
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 3.dp),
        )
    }
}

@Composable
private fun HomeFolderIcon(
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(20.dp)
    ) {
        Icon(
            imageVector = if (selected) AppIcons.FolderOpen else AppIcons.Folder,
            contentDescription = null,
            modifier = Modifier.matchParentSize()
        )
        Icon(
            imageVector = AppIcons.HomeInFolder,
            contentDescription = null,
            tint = AppTheme.colorScheme.background3,
            modifier = Modifier
                .padding(top = 2.5.dp)
                .matchParentSize()
        )
    }
}

@Composable
private fun ProfileIcon(
    selected: Boolean,
    avatarUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .thenIf(selected) {
                border(
                    width = 2.dp,
                    color = AppTheme.colorScheme.foreground,
                    shape = CircleShape
                )
            }
            .size(20.dp)
    ) {
        ProfileImage(
            avatarUrl = avatarUrl,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )
    }
}

@Composable
fun NavigationItems(
    avatarUrl: String?,
    isCompact: Boolean,
    modifier: Modifier = Modifier,
    spacer: (@Composable () -> Unit)? = null,
) {
    val navController = LocalNavController.current

    topLevelRoutes.forEach { tab ->
        if (isCompact && tab.route is Screen.Settings) return@forEach

        val backStackEntry by navController.currentBackStackEntryAsState()
        val selected = backStackEntry?.destination
            ?.hierarchy
            ?.any { it.hasRoute(tab.route::class) }
            ?: false

        if (tab.route is Screen.Settings) {
            spacer?.invoke()
        }

        TabItem(
            modifier = modifier,
            label = stringResource(tab.name),
            selected = selected,
            onItemSelected = { navController.navigateTopDestination(tab.route) },
            isCompact = isCompact,
            icon = {
                when (tab.route) {
                    is Screen.Home -> CalendarIcon(icon = tab.icon)
                    is Screen.Profile -> ProfileIcon(selected = selected, avatarUrl = avatarUrl)
                    is Screen.Notes, is Screen.NoteDetails -> HomeFolderIcon(selected = selected)
                    else -> DefaultIcon(icon = tab.icon)
                }
            }
        )
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun NavigationListenerEffect(navController: NavController) {
    LaunchedEffect(navController) {
        if (!BuildConfig.DEBUG) return@LaunchedEffect

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            Log.d(
                "BackStack",
                controller.currentBackStack.value
                    .map { it.destination.route }
                    .joinToString(", ")
            )

            Log.i(
                "Navigator",
                "Current route: " + destination.hierarchy
                    .mapNotNull { it.route?.substringAfterLast('.') }
                    .toList()
                    .asReversed()
                    .joinToString(separator = "/")
            )
        }
    }
}