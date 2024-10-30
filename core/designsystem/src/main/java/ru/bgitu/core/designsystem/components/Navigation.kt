package ru.bgitu.core.designsystem.components

import android.view.SoundEffectConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.boxShadow

@Composable
fun AppBottomNavigation(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val lineColor = AppTheme.colorScheme.stroke1.copy(.5f)
    val lineWidth = if (AppTheme.isDarkTheme) AppTheme.strokeWidth.thick else 0.dp
    Column(
        modifier = Modifier
            .then(
                if (AppTheme.isDarkTheme) {
                    Modifier.drawBehind {
                        drawLine(
                            color = lineColor,
                            start = Offset.Zero,
                            end = Offset(size.width, 0f),
                            strokeWidth = lineWidth.toPx()
                        )
                    }
                } else {
                    Modifier
                        .boxShadow(
                            blurRadius = 6.dp,
                            alpha = .5f
                        )
                }
            )
            .background(AppTheme.colorScheme.background3)
            .then(modifier)
    ) {
        Row(
            modifier = Modifier.height(AppBottomBarTokens.Height),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun AppRailNavigation(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val lineColor = AppTheme.colorScheme.stroke1.copy(.5f)
    val lineWidth = if (AppTheme.isDarkTheme) AppTheme.strokeWidth.thick else 0.dp

    Column(
        modifier = modifier
            .fillMaxHeight()
            .then(
                if (AppTheme.isDarkTheme) {
                    Modifier.drawBehind {
                        drawLine(
                            color = lineColor,
                            start = Offset(size.width, 0f),
                            end = Offset(size.width, size.height),
                            strokeWidth = lineWidth.toPx()
                        )
                    }
                } else {
                    Modifier
                        .boxShadow(
                            blurRadius = 6.dp,
                            alpha = .5f,
                            offset = DpOffset(x = 2.dp, y = 0.dp)
                        )
                }
            )
            .background(AppTheme.colorScheme.background3)
            .statusBarsPadding()
            .padding(top = AppTheme.spacing.l)
    ) {
        content()
    }
}

@Composable
private fun RailTabItem(
    label: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onItemSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppRippleTheme {
        val background by animateColorAsState(
            targetValue = if (selected) AppTheme.colorScheme.background2 else Color.Transparent
        )
        Surface(
            color = background,
            shape = AppTheme.shapes.default,
            onClick = onItemSelected,
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(AppTheme.spacing.l)
            ) {
                val color by animateColorAsState(
                    targetValue = if (selected) {
                        AppTheme.colorScheme.foreground
                    } else AppTheme.colorScheme.foreground3, label = ""
                )

                CompositionLocalProvider(LocalContentColor provides color) {
                    icon()
                    Text(
                        text = label,
                        style = AppTheme.typography.calloutButton,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomBarTabItem(
    label: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onItemSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val animatedOffset = remember { Animatable(0f) }

    LaunchedEffect(selected) {
        if (!selected) return@LaunchedEffect
        withContext(NonCancellable) {
            animatedOffset.animateTo(
                targetValue = -20f,
                animationSpec = tween(
                    durationMillis = 150,
                    easing = EaseOutBack
                )
            )
            animatedOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 150,
                    easing = EaseOutBack
                )
            )
        }
    }

    Box(
        modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onItemSelected() }
                )
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            val color by animateColorAsState(
                targetValue = if (selected) {
                    AppTheme.colorScheme.foreground
                } else AppTheme.colorScheme.foreground3, label = ""
            )
            CompositionLocalProvider(LocalContentColor provides color) {
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(x = 0, y = animatedOffset.value.toInt())
                        }
                ) {
                    icon()
                }
                Text(
                    text = label,
                    style = AppTheme.typography.caption1,
                    lineHeight = 1.6.em,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            }
        }
    }
}

@Composable
fun TabItem(
    label: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onItemSelected: () -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean,
) {
    val view = LocalView.current

    if (isCompact) {
        BottomBarTabItem(
            label = label,
            icon = icon,
            selected = selected,
            onItemSelected = {
                onItemSelected()
                view.playSoundEffect(SoundEffectConstants.CLICK)
            },
            modifier = modifier
        )
    } else {
        RailTabItem(
            label = label,
            icon = icon,
            selected = selected,
            onItemSelected = {
                onItemSelected()
                view.playSoundEffect(SoundEffectConstants.CLICK)
            },
            modifier = modifier
        )
    }
}

object AppBottomBarTokens {
    val Height = 56.dp
}

private val EaseOutBack = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f)