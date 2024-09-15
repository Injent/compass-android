package ru.bgitu.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.theme.DefaultRippleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppFilledIconButton(
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colorScheme.foreground,
    contentDescription: String? = null,
    shape: Shape = AppTheme.shapes.default
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
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
                painter = painterResource(icon),
                contentDescription = contentDescription,
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
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    iconPadding: PaddingValues = PaddingValues(10.dp),
    tint: Color = LocalContentColor.current,
    iconSize: Dp = 20.dp
) {
    AppRippleTheme(DefaultRippleTheme) {
        Surface(
            color = Color.Transparent,
            shape = AppTheme.shapes.default,
            onClick = onClick,
            modifier = modifier
        ) {
            Box(
                Modifier.padding(iconPadding)
            ) {
                Icon(
                    painter = painterResource(icon),
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
        AppFilledIconButton(onClick = {}, icon = AppIcons.Calendar2)
    }
}

@Preview
@Composable
private fun DefaultAppIconButton() {
    CompassTheme {
        AppIconButton(onClick = {}, icon = AppIcons.Calendar2)
    }
}