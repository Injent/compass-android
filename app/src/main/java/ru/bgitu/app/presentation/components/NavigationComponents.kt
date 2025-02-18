package ru.bgitu.app.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.bgitu.app.navigation.topLevelRoutes
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.datastore.model.UnseenFeatures
import ru.bgitu.core.designsystem.components.TabItem
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.HomeFolder
import ru.bgitu.core.designsystem.icon.HomeFolderOpened
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.nonScaledSp
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
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
    Icon(
        imageVector = if (selected) AppIcons.HomeFolderOpened() else AppIcons.HomeFolder,
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier.size(20.dp)
    )
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
    unseenFeatures: List<Int>,
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current

    val backStackEntry by navController.currentBackStackEntryAsState()
    topLevelRoutes.forEach { tab ->
        if (isCompact && tab.route is Screen.Settings) return@forEach

        val selected = backStackEntry?.destination
            ?.hierarchy
            ?.any { it.hasRoute(tab.route::class) }
            ?: false

        TabItem(
            modifier = modifier,
            label = stringResource(tab.name),
            selected = selected,
            onItemSelected = {
                navController.navigateTopDestination(tab.route)
            },
            isCompact = isCompact,
            icon = {
                Box {
                    when (tab.route) {
                        is Screen.Home -> CalendarIcon(icon = tab.icon)
                        is Screen.Profile -> {
                            ProfileIcon(selected = selected, avatarUrl = avatarUrl)
                            if (UnseenFeatures.PROFILE_FEATURES.any { it in unseenFeatures }) {
                                Badge(
                                    containerColor = AppTheme.colorScheme.foregroundError,
                                    modifier = Modifier
                                        .size(10.dp)
                                        .align(Alignment.TopEnd)
                                        .offset(
                                            x = AppTheme.spacing.xs,
                                            y = -AppTheme.spacing.xs
                                        )
                                )
                            }
                        }
                        is Screen.Notes, is Screen.NoteDetails -> HomeFolderIcon(selected = selected)
                        else -> DefaultIcon(icon = tab.icon)
                    }
                }
            }
        )
    }
}

private fun NavController.navigateTopDestination(tab: Any) {
    if (currentDestination?.hasRoute(tab::class) == true) return

    navigate(tab) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
}