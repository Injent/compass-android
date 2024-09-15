package ru.bgitu.feature.settings.presentation.components

import android.annotation.SuppressLint
import android.view.ContextThemeWrapper
import android.widget.ImageView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.ui.Headline
import ru.bgitu.core.ui.onClick
import ru.bgitu.feature.settings.R

@Composable
fun AppThemeView(
    currentTheme: UiTheme,
    onChangeTheme: (UiTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.mNudge)
    ) {
        Headline(text = stringResource(R.string.app_theme))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            ThemeItem(
                theme = UiTheme.LIGHT,
                selected = currentTheme == UiTheme.LIGHT,
                onClick = { onChangeTheme(UiTheme.LIGHT) }
            )
            ThemeItem(
                theme = UiTheme.DARK,
                isContrast = currentTheme == UiTheme.LIGHT,
                selected = currentTheme == UiTheme.DARK,
                onClick = { onChangeTheme(UiTheme.DARK) }
            )
            ThemeItem(
                theme = UiTheme.SYSTEM,
                isContrast = currentTheme == UiTheme.LIGHT,
                selected = currentTheme == UiTheme.SYSTEM,
                onClick = { onChangeTheme(UiTheme.SYSTEM) }
            )
        }
    }
}

@Composable
private fun ThemeItem(
    theme: UiTheme,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isContrast: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
    ) {
        AppRippleTheme {
            PhoneView(
                theme = theme,
                isContrast = isContrast,
                modifier = Modifier
                    .width(70.dp)
                    .border(
                        width = AppTheme.strokeWidth.thick,
                        color = if (selected) {
                            AppTheme.colorScheme.foreground
                        } else AppTheme.colorScheme.strokeDisabled,
                        shape = AppTheme.shapes.default
                    )
                    .padding(vertical = 5.dp)
                    .clip(AppTheme.shapes.default)
                    .onClick {
                        onClick()
                    }
            )
        }

        Text(
            text = stringResource(
                when (theme) {
                    UiTheme.SYSTEM -> R.string.theme_system
                    UiTheme.LIGHT -> R.string.theme_light
                    UiTheme.DARK -> R.string.theme_dark
                }
            ),
            style = AppTheme.typography.body,
            color = AppTheme.colorScheme.foreground1
        )
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
private fun PhoneView(
    theme: UiTheme,
    isContrast: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var isDarkTheme: Boolean by remember(theme) {
        mutableStateOf(theme == UiTheme.DARK)
    }

    AnimatedContent(
        targetState = isDarkTheme,
        modifier = modifier,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(easing = LinearEasing)
            ) togetherWith fadeOut(
                animationSpec = tween(easing = LinearEasing)
            )
        }
    ) { darkTheme ->
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    setImageDrawable(context.getDrawable(R.drawable.phone_ui))
                }
            },
            update = { imageView ->
                val themeId = if (darkTheme) {
                    if (isContrast) R.style.ThemePhoneUiDarkContrast else R.style.ThemePhoneUiDark
                } else {
                    R.style.ThemePhoneUiLight
                }
                imageView.setImageDrawable(
                    ContextThemeWrapper(context, themeId).getDrawable(R.drawable.phone_ui)
                )
            }
        )
    }

    LaunchedEffect(isDarkTheme, theme) {
        if (theme != UiTheme.SYSTEM) {
            return@LaunchedEffect
        }
        delay(3000)
        isDarkTheme = !isDarkTheme
    }
}

@PreviewLightDark
@Composable
private fun ThemeItempPreview() {
    CompassTheme {
        ThemeItem(
            theme = UiTheme.LIGHT,
            selected = false,
            isContrast = false,
            onClick = {}
        )
    }
}