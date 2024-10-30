package ru.bgitu.feature.home.impl.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.bgitu.core.designsystem.components.AppSmallButton
import ru.bgitu.core.designsystem.illustration.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.home.R

@Composable
fun SelectGroupView(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Image(
            painter = painterResource(AppIllustrations.GroupOfStudents),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Spacer(Modifier.height(AppTheme.spacing.l))
        Text(
            text = stringResource(R.string.your_group),
            style = AppTheme.typography.title2,
            color = AppTheme.colorScheme.foreground1
        )
        Spacer(Modifier.height(AppTheme.spacing.l))
        Text(
            text = stringResource(R.string.select_group_description),
            style = AppTheme.typography.callout,
            color = AppTheme.colorScheme.foreground2,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(AppTheme.spacing.l))
        AppSmallButton(
            text = stringResource(R.string.action_select_group),
            onClick = onClick,
            modifier = Modifier.wrapContentWidth()
        )
    }
}