package ru.bgitu.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.R
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.designsystem.util.thenIf

@Composable
fun Tag(
    text: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.Unspecified,
    hasContainer: Boolean = false
) {
    val backgroundModifier = Modifier
        .background(AppTheme.colorScheme.backgroundTouchable, CircleShape)
        .padding(
            horizontal = AppTheme.spacing.s,
            vertical = AppTheme.spacing.xs
        )
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.thenIf(hasContainer) { backgroundModifier }
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = iconTint,
        )
        Text(
            text = text,
            style = AppTheme.typography.callout,
            color = AppTheme.colorScheme.foreground1
        )
    }
}

enum class StatusDecor {
    DEV,
    AWARDED,
    REGULAR,
    EXPERT
}

@Composable
fun Status(
    statusDecor: StatusDecor,
    modifier: Modifier = Modifier
) {
    val contentColor = when (statusDecor) {
        StatusDecor.DEV -> AppTheme.colorScheme.statusDev
        StatusDecor.AWARDED -> AppTheme.colorScheme.statusAwarded
        StatusDecor.REGULAR -> AppTheme.colorScheme.statusRegular
        StatusDecor.EXPERT -> AppTheme.colorScheme.statusExpert
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                color = contentColor.copy(.15f),
                shape = CircleShape
            )
            .padding(
                horizontal = AppTheme.spacing.s,
                vertical = AppTheme.spacing.xs
            )
    ) {
        Icon(
            painter = painterResource(
                when (statusDecor) {
                    StatusDecor.DEV -> AppIcons.Wrench
                    StatusDecor.AWARDED -> AppIcons.Badge
                    StatusDecor.REGULAR -> AppIcons.Student
                    StatusDecor.EXPERT -> AppIcons.ThumbUp
                }
            ),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = contentColor,
        )
        Text(
            text = stringResource(
                when (statusDecor) {
                    StatusDecor.DEV -> R.string.status_admin
                    StatusDecor.AWARDED -> R.string.status_special
                    StatusDecor.REGULAR -> R.string.status_regular
                    StatusDecor.EXPERT -> R.string.status_expert
                }
            ),
            style = AppTheme.typography.callout,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StatusCard(
    title: String,
    text: String,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.defaultMinSize(minWidth = 200.dp)) {
        Column {
            Spacer(
                Modifier
                    .width(60.dp)
                    .height(4.dp)
                    .background(lineColor)
            )
            Spacer(Modifier.height(AppTheme.spacing.l))
            Text(
                text = title,
                style = AppTheme.typography.headline1,
                color = AppTheme.colorScheme.foreground1
            )
            Spacer(Modifier.height(AppTheme.spacing.s))
            Text(
                text = text,
                style = AppTheme.typography.callout,
                color = AppTheme.colorScheme.foreground1
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 1)
@Composable
private fun StatusOkPreview() {
    CompassTheme {
        Tag(text = "Status", hasContainer = true, icon = AppIcons.Approved)
    }
}