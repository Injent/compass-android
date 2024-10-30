package ru.bgitu.core.designsystem.components

import android.os.Build.VERSION.SDK_INT
import android.view.SoundEffectConstants
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.theme.SpotCard
import ru.bgitu.core.designsystem.util.boxShadow
import ru.bgitu.core.designsystem.util.shadow.roundRectShadow
import ru.bgitu.core.designsystem.util.thenIf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    color: Color = AppTheme.colorScheme.background1,
    shape: Shape = AppTheme.shapes.default,
    shadowEnabled: Boolean = !AppTheme.isDarkTheme,
    contentPadding: PaddingValues = AppCardTokens.ContentPadding,
    contentColor: Color = Color.Unspecified,
    content: @Composable ColumnScope.() -> Unit
) {
    val view = LocalView.current

    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides 0.dp,
    ) {
        val colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = contentColor
        )
        val theModifier = Modifier
            .thenIf(shadowEnabled) {
                if (SDK_INT < 28) {
                    roundRectShadow(
                        offset = DpOffset(0.dp, 2.dp),
                        shape = shape,
                        radius = 10.dp,
                    )
                } else {
                    boxShadow(
                        color = SpotCard,
                        blurRadius = 6.dp,
                        spreadRadius = 0.dp,
                        offset = DpOffset(0.dp, 2.dp),
                        shape = shape,
                        clip = false,
                        inset = false,
                        alpha = .5f
                    )
                }
            }

        val contentInColumn = remember(content, contentPadding) {
            movableContentOf {
                Column(modifier = Modifier.padding(contentPadding), content = content)
            }
        }
        AppRippleTheme {
            onClick?.let {
                Card(
                    onClick = {
                        it.invoke()
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                    },
                    shape = shape,
                    colors = colors,
                    modifier = modifier.then(theModifier),
                ) {
                    contentInColumn()
                }
            } ?: run {
                Card(
                    shape = shape,
                    colors = colors,
                    modifier = modifier.then(theModifier)
                ) {
                    contentInColumn()
                }
            }
        }
    }
}

@Composable
fun AppCardWithContent(
    modifier: Modifier = Modifier,
    label: String? = null,
    action: String? = null,
    onClick: (() -> Unit)? = null,
    shape: Shape = AppTheme.shapes.default,
    content: @Composable ColumnScope.() -> Unit,
) {
    AppCard(
        modifier = modifier,
        shape = shape,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            label?.let {
                Text(
                    text = label,
                    style = AppTheme.typography.body,
                    color = AppTheme.colorScheme.foreground1,
                    modifier = Modifier.weight(1f)
                )
            }
            action?.let {
                Spacer(modifier = Modifier.width(AppTheme.spacing.l))
                Text(
                    text = it,
                    style = AppTheme.typography.subheadlineButton,
                    color = AppTheme.colorScheme.foreground,
                    modifier = Modifier.offset(y = AppTheme.spacing.xs)
                )
            }
        }
        Spacer(Modifier.height(AppTheme.spacing.s))
        content()
    }
}



@Composable
fun AppItemCard(
    @DrawableRes icon: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colorScheme.foreground,
) {
    AppCard(modifier, onClick) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .sizeIn(maxWidth = 20.dp, maxHeight = 20.dp)
            )

            Text(
                text = label,
                style = AppTheme.typography.headline1,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(AppIcons.SmallArrowNext),
                contentDescription = null,
                tint = AppTheme.colorScheme.foreground3
            )
        }
    }
}

@Preview
@Composable
private fun AppItemCardPreview() {
    CompassTheme {
        AppItemCard(
            icon = AppIcons.SettingsOutlined,
            label = "Settings",
            onClick = {},
            modifier = Modifier.width(428.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun AppCardPreview() {
    CompassTheme {
        AppCard(
            onClick = {}
        ) {
            Text(
                text = "Sample text",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

object AppCardTokens {
    val ContentPadding = PaddingValues(
        horizontal = 20.dp,
        vertical = 14.dp
    )
}