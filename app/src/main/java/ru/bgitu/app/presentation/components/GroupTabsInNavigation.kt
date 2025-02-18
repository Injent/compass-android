package ru.bgitu.app.presentation.components

import android.view.SoundEffectConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import ru.bgitu.app.R
import ru.bgitu.core.designsystem.components.AppTab
import ru.bgitu.core.designsystem.components.AppTabTokens
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Swap
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.model.Group

@Composable
fun GroupTabsInNavigation(
    groups: () -> List<Group>,
    selectedGroup: Group?,
    onGroupSelected: (Group) -> Unit,
    onGroupSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val state = rememberLazyListState()

    LazyColumn(
        state = state,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
        contentPadding = PaddingValues(AppTheme.spacing.xs),
        modifier = modifier
            .background(
                color = AppTheme.colorScheme.background4,
                shape = AppTheme.shapes.default
            )
    ) {
        item {
            Surface(
                color = Color.Transparent,
                onClick = onGroupSettingsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTabTokens.Height)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colorScheme.background2, AppTheme.shapes.default)
                        .padding(horizontal = AppTheme.spacing.m)
                ) {
                    Icon(
                        imageVector = AppIcons.Swap,
                        contentDescription = null,
                        tint = AppTheme.colorScheme.foreground3
                    )

                    Text(
                        text = stringResource(R.string.change),
                        color = AppTheme.colorScheme.foreground3,
                        style = AppTheme.typography.calloutButton
                    )
                }
            }
        }
        itemsIndexed(
            items = groups(),
        ) { _, group ->
            AppTab(
                text = group.name,
                selected = selectedGroup == group,
                onClick = {
                    onGroupSelected(group)
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                },
                backgroundColor = animateColorAsState(
                    targetValue = if (selectedGroup == group) {
                        AppTheme.colorScheme.background1
                    } else Color.Transparent
                ).value,
                textAlign = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

    LaunchedEffect(selectedGroup, groups) {
        state.animateScrollToItem(
            index = groups().indexOf(selectedGroup).coerceAtLeast(0)
        )
    }
}