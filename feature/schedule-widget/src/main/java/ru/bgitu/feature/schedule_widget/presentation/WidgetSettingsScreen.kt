package ru.bgitu.feature.schedule_widget.presentation

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppCardWithContent
import ru.bgitu.core.designsystem.components.AppChip
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.boxShadow
import ru.bgitu.feature.schedule_widget.R
import ru.bgitu.feature.schedule_widget.model.WidgetColorScheme
import ru.bgitu.feature.schedule_widget.model.WidgetOptions
import ru.bgitu.feature.schedule_widget.model.WidgetThemeMode
import ru.bgitu.feature.schedule_widget.presentation.component.WidgetPreview
import ru.bgitu.feature.schedule_widget.rememberWidgetOptions
import kotlin.math.round

@SuppressLint("InflateParams")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WidgetSettingsScreen(
    initialState: WidgetOptions,
    onSave: (WidgetOptions) -> Unit,
    onCancel: () -> Unit
) {
    var options by rememberWidgetOptions(widgetOptions = initialState)
    val context = LocalContext.current
    val colors = remember(options.themeMode, options.opacity) {
        WidgetColorScheme.createFrom(context, options.themeMode, opacity = options.opacity)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        modifier = Modifier,
        topBar = {
            val gradientStart = AppTheme.colorScheme.background2
            val gradientEnd = Color.Transparent

            WidgetSettingsTopBar(
                widgetOptions = options,
                colors = colors,
                modifier = Modifier
                    .drawWithCache {
                        val gradientHeightPx = 60f
                        val gradientBrush = Brush.verticalGradient(
                            colors = listOf(gradientStart, gradientEnd),
                            startY = size.height,
                            endY = size.height + gradientHeightPx
                        )
                        onDrawBehind {
                            drawRect(
                                brush = gradientBrush,
                                topLeft = Offset(0f, size.height),
                                size = size.copy(height = gradientHeightPx)
                            )
                        }
                    }
            )
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
                modifier = Modifier
                    .boxShadow(
                        shape = AppTheme.shapes.defaultTopCarved
                    )
                    .background(AppTheme.colorScheme.background3, AppTheme.shapes.defaultTopCarved)
                    .navigationBarsPadding()
                    .padding(
                        horizontal = AppTheme.spacing.l
                    )
            ) {
                AppTextButton(
                    text = stringResource(android.R.string.cancel),
                    onClick = onCancel,
                    color = AppTheme.colorScheme.foreground1,
                    modifier = Modifier.weight(1f)
                )
                AppTextButton(
                    text = stringResource(android.R.string.ok),
                    onClick = { onSave(options) },
                    color = AppTheme.colorScheme.foreground1,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(top = paddingValues.calculateTopPadding())
                .background(AppTheme.colorScheme.background2)
                .padding(AppTheme.spacing.l),
        ) {
            AppCardWithContent(
                label = stringResource(R.string.widget_background)
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s)
                ) {
                    WidgetThemeMode.entries.forEach { widgetThemeMode ->
                        AppChip(
                            selected = widgetThemeMode == options.themeMode,
                            onClick = { options = options.copy(themeMode = widgetThemeMode) },
                            label = stringResource(
                                when (widgetThemeMode) {
                                    WidgetThemeMode.AUTO -> R.string.theme_auto
                                    WidgetThemeMode.LIGHT -> R.string.theme_light
                                    WidgetThemeMode.DARK -> R.string.theme_dark
                                }
                            )
                        )
                    }
                }
                HorizontalDivider(
                    thickness = AppTheme.strokeWidth.thin,
                    color = AppTheme.colorScheme.stroke2,
                    modifier = Modifier.padding(top = AppTheme.spacing.s, bottom = AppTheme.spacing.m)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${((options.opacity * 100).toInt())}%",
                        style = AppTheme.typography.callout,
                        color = AppTheme.colorScheme.foreground1,
                        modifier = Modifier.width(50.dp)
                    )
                    Slider(
                        value = options.opacity,
                        onValueChange = {
                            val alpha = round(it * 10) / 10
                            options = options.copy(opacity = alpha)
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = AppTheme.colorScheme.foreground,
                            activeTrackColor = AppTheme.colorScheme.foreground,
                            inactiveTrackColor = AppTheme.colorScheme.foreground4,
                            inactiveTickColor = AppTheme.colorScheme.stroke2,
                            activeTickColor = AppTheme.colorScheme.foregroundOnBrand
                        ),
                        thumb = {
                            Surface(
                                color = AppTheme.colorScheme.foregroundOnBrand,
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(16.dp)
                                    .border(
                                        width = AppTheme.strokeWidth.large,
                                        color = AppTheme.colorScheme.brandStroke,
                                        shape = CircleShape
                                    )
                            ) {

                            }
                        },
                        steps = 9,
                        valueRange = 0f..1f,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

//            AppCardWithContent(
//                label = stringResource(R.string.widget_button_color)
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                ) {
//                    AppFilledIconButton(
//                        onClick = {},
//                        icon =
//                    )
//                }
//            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WidgetSettingsTopBar(
    widgetOptions: WidgetOptions,
    colors: WidgetColorScheme,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.widget_settings),
                    style = AppTheme.typography.title3,
                    color = AppTheme.colorScheme.foreground1
                )
            }
        )
        val cornerRadiusPx = LocalDensity.current.run {
            AppTheme.spacing.l.toPx()
        }
        WidgetPreview(
            options = widgetOptions,
            colorScheme = colors,
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.s)
                .fillMaxWidth()
                .height(300.dp)
                .drawWithContent {
                    drawRoundRect(
                        color = Color.Transparent,
                        blendMode = BlendMode.Clear,
                        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                    )
                    drawContent()
                }
        )
    }
}

fun <T : View> ViewGroup.getViewsByType(tClass: Class<T>): List<T> {
    return mutableListOf<T?>().apply {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            (child as? ViewGroup)?.let {
                addAll(child.getViewsByType(tClass))
            }
            if (tClass.isInstance(child))
                add(tClass.cast(child))
        }
    }.filterNotNull()
}