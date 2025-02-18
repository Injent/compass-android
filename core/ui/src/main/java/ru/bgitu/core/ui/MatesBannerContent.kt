package ru.bgitu.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.Status
import ru.bgitu.core.designsystem.components.StatusDecor
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.illustration.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.boxShadow

@Composable
fun MatesBannerContent(
    modifier: Modifier = Modifier
) {
    Box(modifier) {
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
            painter = painterResource(AppIcons.ThumbUp),
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
            painter = painterResource(AppIllustrations.SearchMate),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .aspectRatio(1f)
                .offset(x = 8.dp)
        )
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
                    Status(statusDecor = StatusDecor.EXPERT)
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