package ru.bgitu.feature.home.impl.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.shimmer
import ru.bgitu.feature.home.R


@Composable
internal fun NewFeaturesButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppRippleTheme {
        Surface(
            color = AppTheme.colorScheme.background1,
            shape = AppTheme.shapes.default,
            onClick = onClick,
            modifier = modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .shimmer(
                        durationMillis = 3000,
                        delayMillis = 5000,
                        shape = AppTheme.shapes.default,
                        baseColor = Color.Transparent
                    )
                    .padding(
                        horizontal = AppTheme.spacing.l,
                        vertical = AppTheme.spacing.s
                    )

            ) {
                Text(
                    text = stringResource(R.string.try_new_features),
                    style = AppTheme.typography.calloutButton,
                    color = AppTheme.colorScheme.foreground2,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Icon(
                    painter = painterResource(AppIcons.SmallArrowNext),
                    contentDescription = null,
                    tint = AppTheme.colorScheme.foreground3
                )
            }
        }
    }
}