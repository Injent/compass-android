package ru.bgitu.feature.findmate.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.designsystem.components.AppSearchField
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.util.nonScaledSp
import ru.bgitu.core.model.SearchMateItem
import ru.bgitu.core.ui.onClick

@Composable
fun MateView(
    mate: SearchMateItem,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
        modifier = modifier
            .height(82.dp)
            .background(AppTheme.colorScheme.background1)
            .padding(horizontal = AppTheme.spacing.l)
            .onClick { },
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = mate.fullName,
                color = AppTheme.colorScheme.foreground1,
                style = AppTheme.typography.headline2
            )
            Text(
                text = mate.bio,
                color = AppTheme.colorScheme.foreground2,
                style = AppTheme.typography.caption1,
                maxLines = 2
            )
            mate.contacts?.let { contacts ->
                Spacer(Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    contacts.tg?.let {
                        LinkTag(
                            icon = AppIcons.Telegram,
                            url = it,
                            text = it.substringAfterLast('/')
                        )
                    }
                    contacts.vk?.let {
                        LinkTag(
                            icon = AppIcons.VK,
                            url = it,
                            text = it.substringAfterLast('/')
                        )
                    }
                }
            }
        }
        Icon(
            painter = painterResource(AppIcons.SmallArrowNext),
            contentDescription = null,
            tint = AppTheme.colorScheme.foreground3,
        )
    }
}

@Composable
private fun LinkTag(
    @DrawableRes icon: Int,
    text: String,
    url: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(AppTheme.colorScheme.backgroundTouchable, CircleShape)
            .onClick {
                context.openUrl(url)
            }
            .padding(
                start = 4.dp,
                end = 6.dp,
                top = 2.dp,
                bottom = 2.dp
            )
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(
                LocalDensity.current.run {
                    AppTheme.typography.callout.fontSize.nonScaledSp.toDp()
                }
            ),
            tint = Color.Unspecified,
        )
        Text(
            text = text,
            style = AppTheme.typography.caption1,
            color = AppTheme.colorScheme.foreground1
        )
    }
}

@PreviewLightDark
@Composable
private fun MateViewPreview() {
    CompassTheme {
        MateView(
            mate = SearchMateItem(
                userId = 1,
                fullName = "Степанов Алексей",
                bio = "лучше иметь зачет, чем 100 рублей. Покупайте у меня работы",
                isVerified = true,
                contacts = null
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun MateViewList() {
    CompassTheme {
        Column(modifier = Modifier.background(AppTheme.colorScheme.background2)) {
            AppSearchField(
                state = TextFieldState(),
                modifier = Modifier.padding(12.dp)
            )

            repeat(5) {
                MateView(
                    mate = SearchMateItem(
                        userId = it + 1,
                        fullName = "Степанов Алексей",
                        bio = "лучше иметь зачет, чем 100 рублей. Покупайте у меня работы",
                        contacts = null,
                        isVerified = true
                    )
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colorScheme.background1)
                        .padding(
                            start = 72.dp,
                            end = 32.dp
                        ),
                    thickness = 1.dp,
                    color = AppTheme.colors.blueChateau
                )
            }
        }
    }
}