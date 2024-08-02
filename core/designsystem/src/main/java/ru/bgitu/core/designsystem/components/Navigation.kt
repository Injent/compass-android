package ru.bgitu.core.designsystem.components

import android.view.SoundEffectConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.zIndex
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.RedTheme
import ru.bgitu.core.designsystem.util.boxShadow
import kotlin.math.roundToInt

@Composable
fun AppBottomNavigation(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val lineColor = AppTheme.colorScheme.stroke1.copy(.5f)
    val lineWidth = if (AppTheme.isDarkTheme) AppTheme.strokeWidth.thick else 0.dp
    Row(
        modifier = modifier
            .height(AppBottomBarTokens.Height)
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
                        .zIndex(1f)
                        .boxShadow(
                            blurRadius = 6.dp,
                            offset = DpOffset(x = 0.dp, y = (-2).dp),
                            alpha = .5f
                        )
                }
            )
            .background(AppTheme.colorScheme.background3),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
fun AppRailNavigation(
    modifier: Modifier = Modifier,
    containerColor: Color = AppTheme.colorScheme.background1,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .widthIn(min = 72.dp)
            .fillMaxHeight()
            .consumeWindowInsets(WindowInsets.systemBars)
            .background(containerColor)
            .padding(vertical = AppTheme.spacing.xxxl),
        content = content,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xxxl),
        horizontalAlignment = Alignment.CenterHorizontally
    )
}

@Composable
fun TabItem(
    label: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onItemSelected: () -> Unit,
    modifier: Modifier = Modifier,
    badge: Boolean = false,
) {
    val view = LocalView.current
    Box(
        modifier
            .pointerInput(selected) {
                detectTapGestures(
                    onTap = {
                        onItemSelected()
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                    }
                )
            }
    ) {
        if (badge) {
            Spacer(
                Modifier
                    .background(RedTheme, CircleShape)
                    .size(4.dp)
                    .align(Alignment.TopEnd)
            )
        }

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
                val density = LocalDensity.current
                val animatable = remember { Animatable(0f) }

                LaunchedEffect(selected) {
                    if (selected) {
                        animatable.animateTo(
                            targetValue = 0f,
                            animationSpec = keyframes {
                                durationMillis = 300
                                0f at 0 using FastOutLinearInEasing
                                0.8f at 100 using  FastOutSlowInEasing
                                1f at 150 // ms
                                0f at 300 using CubicBezierEasing(1f, 0.2f, 0.0f, 0.4f)
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier.offset {
                        IntOffset(
                            x = 0,
                            y = density.run { -(animatable.value * 8.dp.toPx()).roundToInt() }
                        )
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

object AppBottomBarTokens {
    val Height = 56.dp
}