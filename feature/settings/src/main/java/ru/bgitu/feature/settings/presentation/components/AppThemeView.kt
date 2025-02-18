package ru.bgitu.feature.settings.presentation.components

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.view.ContextThemeWrapper
import android.view.SoundEffectConstants
import android.widget.ImageView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.bgitu.core.designsystem.components.AppCard
import ru.bgitu.core.designsystem.components.AppCardTokens
import ru.bgitu.core.designsystem.components.AppSwitch
import ru.bgitu.core.designsystem.theme.AppRippleTheme
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme
import ru.bgitu.core.model.settings.UiTheme
import ru.bgitu.core.ui.Headline
import ru.bgitu.feature.settings.R
import ru.bgitu.feature.settings.presentation.settings.SettingsIntent
import ru.bgitu.feature.settings.presentation.settings.SettingsUiState

@Composable
fun AppThemeView(
    uiState: SettingsUiState.Success,
    onIntent: (SettingsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current

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
                selected = uiState.prefs.theme == UiTheme.LIGHT,
                onClick = {
                    onIntent(SettingsIntent.ChangeUiTheme(UiTheme.LIGHT))
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                }
            )
            ThemeItem(
                theme = UiTheme.DARK,
                isContrast = uiState.prefs.theme == UiTheme.LIGHT,
                selected = uiState.prefs.theme == UiTheme.DARK,
                onClick = {
                    onIntent(SettingsIntent.ChangeUiTheme(UiTheme.DARK))
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                }
            )
            ThemeItem(
                theme = UiTheme.SYSTEM,
                isContrast = uiState.prefs.theme == UiTheme.LIGHT,
                selected = uiState.prefs.theme == UiTheme.SYSTEM,
                onClick = {
                    onIntent(SettingsIntent.ChangeUiTheme(UiTheme.SYSTEM))
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                }
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.colorScheme.stroke2
        )
        if (SDK_INT >= 31) {
            AppCard(
                contentPadding = PaddingValues(),
                onClick = {
                    onIntent(SettingsIntent.UseDynamicTheme(!uiState.prefs.useDynamicTheme))
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppCardTokens.ContentPadding)
                ) {
                    Text(
                        text = stringResource(R.string.use_dynamic_theme),
                        style = AppTheme.typography.headline2,
                        color = AppTheme.colorScheme.foreground1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = AppTheme.spacing.l)
                    )
                    AppSwitch(
                        checked = uiState.prefs.useDynamicTheme,
                        onCheckedChange = {
                            onIntent(SettingsIntent.UseDynamicTheme(it))
                        },
                        modifier = Modifier
                    )
                }
            }
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
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
    ) {
        AppRippleTheme {
            val animatedScale = remember { Animatable(1f) }

            PhoneView(
                theme = theme,
                isContrast = isContrast,
                modifier = Modifier
                    .scale(animatedScale.value)
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
                    .clickable(interactionSource = null, indication = null) {
                        onClick()
                        coroutineScope.launch(NonCancellable) {
                            animatedScale.animateTo(
                                targetValue = 1.05f
                            )
                            animatedScale.animateTo(1f)
                        }
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
        val dynamicThemeEnabled = AppTheme.isDynamicThemeEnabled

        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    setImageDrawable(context.getDrawable(R.drawable.phone_ui))
                }
            },
            update = { imageView ->
                val themeId = when {
                    SDK_INT >= 31 && dynamicThemeEnabled && darkTheme && isContrast ->
                        R.style.ThemePhoneUiDynamicDark_Contrast
                    SDK_INT >= 31 && dynamicThemeEnabled && darkTheme ->
                        R.style.ThemePhoneUiDynamicDark
                    SDK_INT >= 31 && dynamicThemeEnabled -> R.style.ThemePhoneUiDynamicLight
                    darkTheme && isContrast -> R.style.ThemePhoneUiDarkContrast
                    darkTheme -> R.style.ThemePhoneUiDark
                    else -> R.style.ThemePhoneUiLight
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