package ru.bgitu.feature.findmate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppSearchField
import ru.bgitu.core.designsystem.components.DynamicAsyncImage
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Telegram
import ru.bgitu.core.designsystem.icon.Vk
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.util.nonScaledSp
import ru.bgitu.core.model.Contacts
import ru.bgitu.core.model.SearchMateItem

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
            .clip(AppTheme.shapes.default)
            .clickable { }
            .padding(horizontal = AppTheme.spacing.l),
    ) {
        DynamicAsyncImage(
            imageUrl = mate.avatarUrl,
            modifier = Modifier
                .fillMaxHeight(.5f)
                .aspectRatio(1f)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row {
                Text(
                    text = mate.fullName,
                    color = AppTheme.colorScheme.foreground1,
                    style = AppTheme.typography.headline2
                )

            }
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
                            icon = AppIcons.Vk,
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
    icon: ImageVector,
    text: String,
    url: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(AppTheme.colorScheme.background3, AppTheme.shapes.small)
            .padding(
                start = 4.dp,
                end = 6.dp,
                top = 2.dp,
                bottom = 2.dp
            )
    ) {
        Icon(
            imageVector = icon,
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
                avatarUrl = "https://sun126-1.userapi.com/s/v1/ig2/I0rD4tt1gPhipJT-lGDMFCSDJLVY_-cy7XkAkf0kkiWUbDCzZrcAc1yo8AaBGMSbQKuCXTx2nAcXz5kOE2nGrMG-.jpg?size=50x50&quality=95&crop=42,114,525,525&ava=1",
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
                        fullName = "Веревкин Елисей",
                        bio = "лучше иметь зачет, чем 100 рублей. Покупайте у меня работы",
                        contacts = Contacts(
                            tg = "tg.me/injent",
                            vk = "vk.com/Injent"
                        ),
                        avatarUrl = "https://sun126-1.userapi.com/s/v1/ig2/I0rD4tt1gPhipJT-lGDMFCSDJLVY_-cy7XkAkf0kkiWUbDCzZrcAc1yo8AaBGMSbQKuCXTx2nAcXz5kOE2nGrMG-.jpg?size=50x50&quality=95&crop=42,114,525,525&ava=1",
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
                    color = AppTheme.colorScheme.stroke1
                )
            }
        }
    }
}

@Composable
private fun ConfirmedCheck(
    modifier: Modifier = Modifier
) {

}