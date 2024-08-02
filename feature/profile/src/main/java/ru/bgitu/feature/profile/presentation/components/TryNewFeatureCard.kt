package ru.bgitu.feature.profile.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppIconButton
import ru.bgitu.core.designsystem.components.Status
import ru.bgitu.core.designsystem.components.StatusDecor
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.util.boxShadow
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
            val iconTint = if (AppTheme.isDarkTheme) {
                AppTheme.colorScheme.foreground4
            } else AppTheme.colorScheme.background4
            Icon(
                painter = painterResource(AppIcons.Badge),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(60.dp)
                    .offset(x = 15.dp, y = (-70).dp)
                    .rotate(-25f)
            )
            Icon(
                painter = painterResource(AppIcons.BookCover),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(60.dp)
                    .offset(x = (-20).dp, y = 76.dp)
                    .rotate(10f)
            )
            FakeMateCard(
                fullName = R.string.fakeProfile1_fullName,
                bio = R.string.fakeProfile1_bio,
                profileImage = R.drawable.fake_profile1,
                trusted = true,
                modifier = Modifier
                    .rotate(-15f)
                    .offset(x = (-10).dp, y = 15.dp)
                    .fillMaxWidth(.95f)
                    .boxShadow(
                        shape = AppTheme.shapes.small
                    )
            )
            FakeMateCard(
                fullName = R.string.fakeProfile2_fullName,
                bio = R.string.fakeProfile2_bio,
                profileImage = R.drawable.fake_profile2,
                trusted = false,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .rotate(10f)
                    .fillMaxWidth(.7f)
                    .offset(x = 30.dp, y = (-65).dp)
                    .boxShadow(
                        shape = AppTheme.shapes.small
                    )
            )
            Image(
                painter = painterResource(AppIllustrations.SearhMate),
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

@Composable
private fun FakeMateCard(
    @StringRes fullName: Int,
    @StringRes bio: Int,
    @DrawableRes profileImage: Int,
    trusted: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
        modifier = modifier
            .height(65.dp)
            .background(
                if (AppTheme.isDarkTheme) {
                    AppTheme.colorScheme.background3
                } else AppTheme.colorScheme.background1
            )
            .clip(AppTheme.shapes.default)
            .padding(horizontal = AppTheme.spacing.m),
    ) {
        Image(
            painter = painterResource(profileImage),
            contentDescription = null,
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(fullName),
                    color = AppTheme.colorScheme.foreground1,
                    style = AppTheme.typography.headline2
                )
                if (trusted) {
                    Status(statusDecor = StatusDecor.POPULAR)
                }
            }
            Text(
                text = stringResource(bio),
                color = AppTheme.colorScheme.foreground2,
                style = AppTheme.typography.caption1,
                maxLines = 2
            )
        }
    }
}

@Preview
@Composable
private fun FakeMateCardPreview() {
    CompassTheme {
        Column {
            FakeMateCard(
                fullName = R.string.fakeProfile1_fullName,
                bio = R.string.fakeProfile1_bio,
                profileImage = R.drawable.fake_profile1,
                trusted = true
            )
            FakeMateCard(
                fullName = R.string.fakeProfile2_fullName,
                bio = R.string.fakeProfile2_bio,
                profileImage = R.drawable.fake_profile2,
                trusted = false
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