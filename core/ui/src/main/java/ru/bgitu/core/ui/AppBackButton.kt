package ru.bgitu.core.ui

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Back2
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun AppBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current

    AppRippleTheme {
        Surface(
            color = Color.Transparent,
            shape = AppTheme.shapes.default,
            onClick = {
                onClick()
                view.playSoundEffect(SoundEffectConstants.CLICK)
            },
            modifier = modifier
        ) {
            Box(
                Modifier.padding(PaddingValues(10.dp))
            ) {
                Icon(
                    imageVector = AppIcons.Back2,
                    contentDescription = null,
                    tint = AppTheme.colorScheme.foreground3,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(18.dp)
                )
            }
        }
    }
}