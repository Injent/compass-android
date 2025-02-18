package ru.bgitu.feature.groups.presentation.component

import android.os.Build.VERSION.SDK_INT
import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.layout.DraggableItem
import ru.bgitu.core.designsystem.layout.dragContainer
import ru.bgitu.core.designsystem.layout.rememberDragDropState
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.DefaultRippleConfig
import ru.bgitu.core.designsystem.theme.NoRippleConfig
import ru.bgitu.core.designsystem.util.AutoSizeText
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.model.Group
import ru.bgitu.feature.groups.R

@Composable
internal fun ReorderableGroupTabs(
    groups: List<Group>,
    onChange: (List<Group>) -> Unit,
    onGroupClick: (Group) -> Unit,
    enabled: Boolean,
    showHint: Boolean = true,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    // Duplicate is required for correct composition and animations
    var list by remember {
        mutableStateOf(groups)
    }

    LaunchedEffect(groups) {
        if (groups.size != list.size) {
            list = groups
        }
    }

    Column(modifier) {
        val alpha by animateFloatAsState(
            targetValue = if (enabled) 1f else .25f
        )

        if (showHint) {
            Text(
                text = if (groups.size <= 1) {
                    stringResource(R.string.add_groups)
                } else stringResource(R.string.organize_groups),
                style = AppTheme.typography.subheadline,
                color = AppTheme.colorScheme.foreground2,
                modifier = Modifier
                    .alpha(alpha)
                    .padding(bottom = AppTheme.spacing.s)
            )
        }

        val listState = rememberLazyListState()
        val dragDropState = rememberDragDropState(listState) { from, to ->
            list = list.toMutableList().apply {
                add(to, removeAt(from))
            }
            onChange(list)
        }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(AppTheme.spacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
            modifier = Modifier
                .fillMaxWidth()
                .thenIf(enabled) {
                    dragContainer(dragDropState)
                }
                .background(
                    color = AppTheme.colorScheme.background4,
                    shape = AppTheme.shapes.default
                )
                .alpha(alpha),
        ) {
            if (groups.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.noGroups),
                        style = AppTheme.typography.callout,
                        color = AppTheme.colorScheme.foreground3,
                        modifier = Modifier
                            .padding(vertical = AppTheme.spacing.l)
                    )
                }
            }

            itemsIndexed(
                items = list,
                key = { _, group -> group.id }
            ) { index, group ->
                DraggableItem(
                    dragDropState = dragDropState,
                    index = index,
                    modifier = Modifier
                        .fillMaxWidth()
                ) { isDragging ->
                    LaunchedEffect(isDragging) {
                        val hapticConstant = when {
                            SDK_INT >= 34 -> {
                                if (isDragging) HapticFeedbackConstants.DRAG_START else HapticFeedbackConstants.CONFIRM
                            }
                            isDragging -> HapticFeedbackConstants.LONG_PRESS
                            else -> HapticFeedbackConstants.CONTEXT_CLICK
                        }
                        view.performHapticFeedback(hapticConstant)
                    }
                    ReorderableGroupTab(
                        group = group,
                        onClick = { onGroupClick(group) },
                        isDragging = isDragging,
                        enabled = enabled,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ReorderableGroupTab(
    group: Group,
    onClick: () -> Unit,
    isDragging: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isDragging) {
            AppTheme.colorScheme.backgroundBrand
        } else AppTheme.colorScheme.backgroundTouchable
    )
    val textColor by animateColorAsState(
        targetValue = if (isDragging) {
            AppTheme.colorScheme.foregroundOnBrand
        } else AppTheme.colorScheme.foreground1
    )
    val iconColor by animateColorAsState(
        targetValue = if (isDragging) {
            AppTheme.colorScheme.foregroundOnBrand
        } else AppTheme.colorScheme.foreground3
    )

    AppRippleTheme(if (enabled) DefaultRippleConfig else NoRippleConfig) {
        Surface(
            color = backgroundColor,
            shape = AppTheme.shapes.default,
            onClick = onClick,
            enabled = enabled,
            modifier = modifier
                .height(48.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(AppTheme.spacing.s)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = null,
                    tint = iconColor
                )
                AutoSizeText(
                    text = group.name,
                    style = AppTheme.typography.calloutButton,
                    color = textColor,
                )
            }
        }
    }
}