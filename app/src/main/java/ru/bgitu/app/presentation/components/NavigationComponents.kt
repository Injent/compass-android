package ru.bgitu.app.presentation.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.bgitu.app.BuildConfig
import ru.bgitu.core.common.DateTimeUtil
import ru.bgitu.core.designsystem.components.TabItem
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.nonScaledSp
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.Screen
import ru.bgitu.core.navigation.Tab
import ru.bgitu.core.navigation.getId
import ru.bgitu.core.navigation.navigateTopDestination
import ru.bgitu.core.ui.ProfileImage

@Composable
fun DefaultIcon(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = null,
        modifier = modifier.size(AppTheme.spacing.xl)
    )
}

@Composable
fun CalendarIcon(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier.size(AppTheme.spacing.xl)) {
        Icon(
            painter = painterResource(icon),
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
    hideMateTab: Boolean,
    modifier: Modifier = Modifier
) {
    val navController = LocalNavController.current

    Tab.entries.forEach { tab ->
        if (tab == Tab.MATES && hideMateTab) return@forEach

        val selected = navController.currentBackStackEntryAsState()
            .value
            ?.destination
            ?.isCurrentTab(tab)
            ?: false

        TabItem(
            modifier = modifier,
            label = stringResource(tab.label),
            selected = selected,
            onItemSelected = { navController.navigateTopDestination(tab) },
            icon = {
                when (tab) {
                    Tab.HOME -> CalendarIcon(icon = tab.icon)
                    Tab.PROFESSOR_SEARCH, Tab.MATES -> DefaultIcon(icon = tab.icon)
                    Tab.PROFILE -> ProfileIcon(selected = selected, avatarUrl = avatarUrl)
                }
            }
        )
    }
}

private fun NavDestination.isCurrentTab(tab: Tab): Boolean {
    return when (tab) {
        Tab.HOME -> id == getId<Screen.Home>()
        Tab.PROFESSOR_SEARCH -> id == getId<Screen.TeacherSearch>()
        Tab.MATES -> id == getId<Screen.SearchMate>()
        Tab.PROFILE -> id == getId<Screen.Profile>()
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