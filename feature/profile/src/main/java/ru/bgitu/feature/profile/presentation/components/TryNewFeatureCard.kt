package ru.bgitu.feature.profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppIconButton
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Close
import ru.bgitu.core.designsystem.illustration.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.ui.MatesBannerContent
import ru.bgitu.feature.profile.R

@Composable
fun TryNewFeatureCard(
    onClose: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues()
    ) {
        Box {
            MatesBannerContent(
                modifier = Modifier.matchParentSize()
            )
            Image(
                painter = painterResource(AppIllustrations.SearchMate),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .offset(x = 8.dp)
            )
            Surface(
                color = AppTheme.colorScheme.backgroundBrand,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.try_find_mates),
                    style = AppTheme.typography.title3,
                    color = AppTheme.colorScheme.foregroundOnBrand,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(
                            horizontal = AppTheme.spacing.xxl,
                            vertical = AppTheme.spacing.m
                        )
                )
            }
            AppIconButton(
                onClick = onClose,
                icon = AppIcons.Close,
                tint = AppTheme.colorScheme.foreground4,
                modifier = Modifier
                    .align(Alignment.TopEnd)
            )
        }
    }
}

@PreviewLightDark
@Preview(locale = "ru-ru")
@Composable
private fun TryNewFeatureCardPreview() {
    CompassTheme {
        TryNewFeatureCard(
            onClick = {},
            onClose = {}
        )
    }
}