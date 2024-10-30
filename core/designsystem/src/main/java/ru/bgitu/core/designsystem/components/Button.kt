package ru.bgitu.core.designsystem.components

import android.view.SoundEffectConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Close
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.theme.DefaultRippleConfig
import ru.bgitu.core.designsystem.theme.NoRippleConfig
import ru.bgitu.core.designsystem.util.thenIf

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = AppTheme.shapes.default,
    isLoading: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp)
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsFocusedAsState()

    val containerColor by animateColorAsState(
        targetValue = when {
            !enabled -> AppTheme.colorScheme.backgroundDisabled
            isPressed -> AppTheme.colorScheme.backgroundPressed
            else -> AppTheme.colorScheme.backgroundBrand
        },
        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> AppTheme.colorScheme.foregroundOnBrand.copy(.75f)
            isPressed -> AppTheme.colorScheme.foregroundOnBrand
                .copy(.5f)
                .compositeOver(AppTheme.colorScheme.background1)
            else -> AppTheme.colorScheme.foregroundOnBrand
        }
    )

    BaseButton(
        onClick = onClick,
        modifier = modifier
            .height(ButtonTokens.LargeButtonHeight),
        enabled = enabled,
        shape = shape,
        isLoading = isLoading,
        containerColor = containerColor,
        contentColor = contentColor,
        contentPadding = contentPadding
    ) {
        AppTextWithLoading(
            text = text,
            isLoading = isLoading,
            loadingSize = 26.dp
        )
    }
}

@Composable
fun AppSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = AppTheme.shapes.default,
    isLoading: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsFocusedAsState()

    val containerColor by animateColorAsState(
        targetValue = when {
            !enabled -> AppTheme.colorScheme.backgroundTouchable.copy(.5f)
            isPressed -> AppTheme.colorScheme.backgroundTouchablePressed
            else -> AppTheme.colorScheme.backgroundTouchable
        }
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> AppTheme.colorScheme.foreground1.copy(.35f)
            isPressed -> AppTheme.colorScheme.foreground1.copy(.75f)
            else -> AppTheme.colorScheme.foreground
        }
    )

    BaseButton(
        onClick = onClick,
        isLoading = isLoading,
        enabled = enabled,
        containerColor = containerColor,
        contentColor = contentColor,
        shape = shape,
        rippleConfig = DefaultRippleConfig,
        modifier = modifier
            .height(ButtonTokens.LargeButtonHeight)
    ) {
        AppTextWithLoading(
            text = text,
            isLoading = isLoading,
            loadingSize = 26.dp
        )
    }
}

@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = AppTheme.spacing.xs),
    shape: Shape = AppTheme.shapes.small,
    color: Color = AppTheme.colorScheme.foreground,
    enabled: Boolean = true
) {
    AppRippleTheme {
        TextButton(
            onClick = { if (enabled) onClick() },
            modifier = modifier
                .thenIf(!enabled) {
                    alpha(.5f)
                },
            contentPadding = contentPadding,
            shape = shape,
            colors = ButtonDefaults.textButtonColors(
                contentColor = color,
                disabledContentColor = AppTheme.colorScheme.foregroundDisabled
            ),
        ) {
            Text(
                text = text,
                style = AppTheme.typography.subheadlineButton
            )
        }
    }
}

@Composable
private fun AppTextWithLoading(
    text: String,
    textStyle: TextStyle = AppTheme.typography.bodyButton,
    loadingSize: Dp,
    isLoading: Boolean = false
) {
    Box {
        val alphaTransitionText by animateFloatAsState(
            targetValue = if (isLoading) 0f else 1f,
            animationSpec = tween(), label = ""
        )
        val scaleTransitionText by animateFloatAsState(
            targetValue = if (isLoading) 0.75f else 1f, label = ""
        )
        val alphaTransitionLoading by animateFloatAsState(
            targetValue = if (isLoading) 1f else 0f, label = ""
        )

        Text(
            text = text,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .alpha(alphaTransitionText)
                .scale(scaleTransitionText)
                .align(Alignment.Center),
            color = LocalContentColor.current
        )

        if (isLoading)
            AppCircularLoading(
                modifier = Modifier
                    .size(loadingSize)
                    .alpha(alphaTransitionLoading)
                    .align(Alignment.Center)
            )
    }
}

@Composable
private fun BaseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean = true,
    shape: Shape = AppTheme.shapes.default,
    isLoading: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp),
    rippleConfig: RippleConfiguration? = NoRippleConfig,
    content: @Composable RowScope.() -> Unit
) {
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.975f else 1f,
        animationSpec = tween(durationMillis = 0)
    )

    AppRippleTheme(rippleConfig) {
        Surface(
            color = containerColor,
            contentColor = contentColor,
            onClick = {
                if (!isLoading) {
                    onClick()
                }
                view.playSoundEffect(SoundEffectConstants.CLICK)
            },
            interactionSource = interactionSource,
            shape = shape,
            enabled = enabled,
            modifier = modifier
                .scale(buttonScale)
        ) {
            Row(
                modifier = Modifier.padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@Composable
fun AppSmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CircleShape,
    isLoading: Boolean = false,
    icon: ImageVector? = null
) {
    BaseButton(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        isLoading = isLoading,
        contentPadding = PaddingValues(horizontal = AppTheme.spacing.m),
        containerColor = AppTheme.colorScheme.backgroundBrand,
        contentColor = AppTheme.colorScheme.foregroundOnBrand,
        modifier = modifier.height(ButtonTokens.SmallButtonHeight),
    ) {
        val textStyle = AppTheme.typography.calloutButton.copy(fontWeight = FontWeight.Medium)
        icon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LocalContentColor.current,
                modifier = Modifier.size(
                    LocalDensity.current.run { textStyle.lineHeight.toDp() - 2.dp }
                )
            )
        }
        Spacer(Modifier.weight(1f))
        AppTextWithLoading(
            text = text,
            loadingSize = 18.dp,
            textStyle = textStyle,
            isLoading = isLoading
        )
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun AppConfirmButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsFocusedAsState()

    val containerColor by animateColorAsState(
        targetValue = when {
            !enabled -> AppTheme.colorScheme.backgroundDisabled
            isPressed -> AppTheme.colorScheme.backgroundPressed
            else -> AppTheme.colorScheme.backgroundBrand
        },
        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> AppTheme.colorScheme.foregroundOnBrand.copy(.35f)
            isPressed -> AppTheme.colorScheme.foregroundOnBrand.copy(.75f)
            else -> AppTheme.colorScheme.foregroundOnBrand
        }
    )

    BaseButton(
        onClick = onClick,
        containerColor = containerColor,
        contentColor = contentColor,
        enabled = enabled,
        modifier = modifier.height(58.dp),
        contentPadding = PaddingValues(horizontal = AppTheme.spacing.s, vertical = AppTheme.spacing.s)
    ) {
        Icon(
            painter = painterResource(AppIcons.Done),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            style = AppTheme.typography.calloutButton,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = AppTheme.spacing.s)
        )
    }
}

@Composable
fun AppDismissButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    contentColor: Color,
) {
    BaseButton(
        onClick = onClick,
        containerColor = contentColor.copy(.1f),
        contentColor = contentColor,
        modifier = modifier.height(58.dp),
        contentPadding = PaddingValues(horizontal = AppTheme.spacing.s, vertical = AppTheme.spacing.s)
    ) {
        Icon(
            imageVector = AppIcons.Close,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            style = AppTheme.typography.calloutButton,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = AppTheme.spacing.s)
        )
    }
}

@Composable
fun AppRippleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = AppTheme.colorScheme.backgroundTouchable.copy(alpha = .65f),
    textColor: Color = AppTheme.colorScheme.foreground1,
    shape: Shape = RectangleShape,
    contentPadding: PaddingValues = PaddingValues(horizontal = AppTheme.spacing.s)
) {
    AppRippleTheme {
        Box(
            modifier = modifier
                .height(ButtonTokens.LargeButtonHeight)
                .background(
                    color = containerColor,
                    shape = shape
                )
                .clip(shape)
                .clickable { onClick() }
        ) {
            Text(
                text = text,
                style = AppTheme.typography.calloutButton,
                color = textColor,
                modifier = modifier
                    .align(Alignment.Center)
                    .padding(contentPadding),
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFF,
    widthDp = 344
)
@Composable
private fun DefaultAppButtonPreview() {
    CompassTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(AppTheme.spacing.l)
        ) {
            var isLoading by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            AppButton(
                text = "Text button",
                onClick = {
                    isLoading = true
                    scope.launch {
                        delay(5000)
                        isLoading = false
                    }
                },
                isLoading = isLoading
            )

            AppSmallButton(
                text = "Text button",
                onClick = {
                    isLoading = true
                    scope.launch {
                        delay(5000)
                        isLoading = false
                    }
                },
                isLoading = isLoading
            )
        }
    }
}

private object ButtonTokens {
    val LargeButtonHeight = 48.dp
    val SmallButtonHeight = 36.dp
}