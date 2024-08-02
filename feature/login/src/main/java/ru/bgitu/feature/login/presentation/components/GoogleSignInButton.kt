package ru.bgitu.feature.login.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.feature.login.R

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = AppTheme.colorScheme.backgroundTouchable,
        shape = AppTheme.shapes.default,
        modifier = modifier
            .height(48.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 14.dp)
        ) {
            val iconSizeModifier = Modifier.size(20.dp)
            Icon(
                painter = painterResource(AppIcons.Google),
                contentDescription = null,
                modifier = Modifier.then(iconSizeModifier),
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.sign_in_with_google),
                style = MaterialTheme.typography.labelLarge,
                color = if (AppTheme.isDarkTheme) {
                    GoogleSignInButtonTokens.TextColorDark
                } else GoogleSignInButtonTokens.TextColorLight,
                fontSize = 17.sp
            )
            Spacer(iconSizeModifier)
        }
    }
}

object GoogleSignInButtonTokens {
    val TextColorDark = Color(0xFFE0E0E0)
    val TextColorLight = Color(0xFF2C2C2C)
}