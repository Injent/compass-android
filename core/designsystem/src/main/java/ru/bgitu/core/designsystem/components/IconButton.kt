package ru.bgitu.core.designsystem.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.CalendarOutlined
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme

@Composable
fun AppFilledIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colorScheme.foreground,
    contentDescription: String? = null,
    shape: Shape = AppTheme.shapes.default
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 40.dp) {
        FilledIconButton(
            onClick = onClick,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = AppTheme.colorScheme.background1,
                contentColor = tint,
                disabledContainerColor = AppTheme.colorScheme.foreground3,
                disabledContentColor = AppTheme.colorScheme.foreground4
            ),
            shape = shape,
            modifier = modifier.defaultMinSize(40.dp, 40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.sizeIn(
                    minWidth = 12.dp, minHeight = 12.dp,
                    maxWidth = 20.dp, maxHeight = 20.dp
                )
            )
        }
    }
}

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconPadding: PaddingValues = PaddingValues(10.dp),
    tint: Color = LocalContentColor.current,
    iconSize: Dp = 20.dp
) {
    val view = LocalView.current
    AppRippleTheme {
        Surface(
            color = Color.Transparent,
            shape = AppTheme.shapes.default,
            onClick = {
                onClick()
                view.playSoundEffect(SoundEffectConstants.CLICK)
            },
            modifier = modifier
        ) {
            Box(
                Modifier.padding(iconPadding)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(iconSize)
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefaultAppFilledIconButton() {
    CompassTheme {
        AppFilledIconButton(onClick = {}, icon = AppIcons.CalendarOutlined)
    }
}

@Preview
@Composable
private fun DefaultAppIconButton() {
    CompassTheme {
        AppIconButton(onClick = {}, icon = AppIcons.CalendarOutlined)
    }
}