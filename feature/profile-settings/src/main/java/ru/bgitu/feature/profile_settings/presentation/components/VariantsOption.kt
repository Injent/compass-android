package ru.bgitu.feature.profile_settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppSecondaryButton
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.boxShadow
import ru.bgitu.core.model.UserProfile
import ru.bgitu.core.ui.Headline
import ru.bgitu.feature.profile_settings.R

@Composable
fun VariantsOption(
    variants: List<UserProfile.VariantEntry>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Headline(
            text = stringResource(R.string.variants),
            modifier = Modifier.padding(bottom = AppTheme.spacing.s)
        )
        AppCard(
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = 4.dp,
                bottomEnd = 4.dp
            ),
            color = AppTheme.colorScheme.background1,
            contentPadding = PaddingValues(),
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs)) {
                Text(
                    text = stringResource(R.string.your_variants),
                    style = AppTheme.typography.body,
                    color = AppTheme.colorScheme.foreground1,
                    modifier = Modifier.padding(
                        top = AppTheme.spacing.l,
                        start = AppTheme.spacing.l,
                        end = AppTheme.spacing.l,
                    )
                )
                Text(
                    text = stringResource(R.string.variants_description),
                    color = AppTheme.colorScheme.foreground2,
                    style = AppTheme.typography.callout,
                    modifier = Modifier.padding(
                        top = AppTheme.spacing.xs,
                        start = AppTheme.spacing.l,
                        end = AppTheme.spacing.l,
                        bottom = AppTheme.spacing.l
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(AppTheme.spacing.xs))
        VariantView(variant = UserProfile.VariantEntry("ale", 1))
        variants.forEach { variant ->
            Spacer(modifier = Modifier.height(AppTheme.spacing.xs))
            VariantView(variant = variant)
        }
        Spacer(modifier = Modifier.height(AppTheme.spacing.xs))
        val buttonShape = RoundedCornerShape(
            topStart = 4.dp,
            topEnd = 4.dp,
            bottomStart = 12.dp,
            bottomEnd = 12.dp
        )
        AppSecondaryButton(
            text = stringResource(R.string.action_add),
            shape = buttonShape,
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .boxShadow(
                    shape = buttonShape,
                    blurRadius = 6.dp,
                    alpha = .5f
                )
        )
    }
}

@Composable
private fun VariantView(
    variant: UserProfile.VariantEntry,
    modifier: Modifier = Modifier
) {
    AppCard(
        shape = AppTheme.shapes.extraSmall,
        color = AppTheme.colorScheme.background1,
        contentPadding = PaddingValues(),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(end = AppTheme.spacing.m)
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(AppTheme.colorScheme.backgroundBrand)
            )
            Text(
                text = variant.subjectName,
                style = AppTheme.typography.body,
                color = AppTheme.colorScheme.foreground1,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = AppTheme.spacing.m)
                    .background(AppTheme.colorScheme.background2, AppTheme.shapes.default)
                    .padding(AppTheme.spacing.s)
            )
            Text(
                text = variant.variant.toString(),
                style = AppTheme.typography.body,
                color = AppTheme.colorScheme.foreground1,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .defaultMinSize(minWidth = 40.dp)
                    .background(AppTheme.colorScheme.background2, AppTheme.shapes.default)
                    .padding(AppTheme.spacing.s)
            )
        }
    }
}