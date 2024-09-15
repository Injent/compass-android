package ru.bgitu.feature.schedule_widget.presentation

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.components.AppCardWithContent
import ru.bgitu.core.designsystem.components.AppChip
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.boxShadow
import ru.bgitu.feature.schedule_widget.R
import ru.bgitu.feature.schedule_widget.model.WidgetColors
import ru.bgitu.feature.schedule_widget.model.WidgetOptions
import ru.bgitu.feature.schedule_widget.model.WidgetTextColor
import ru.bgitu.feature.schedule_widget.model.WidgetTheme
import ru.bgitu.feature.schedule_widget.model.provideWidgetColors
import ru.bgitu.feature.schedule_widget.rememberWidgetOptions
import ru.bgitu.feature.schedule_widget.presentation.component.WidgetPreview
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
    val colors = remember(options.widgetTheme) {
        provideWidgetColors(context, options)
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
                label = stringResource(R.string.widget_opacity)
            ) {
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
                        steps = 9,
                        valueRange = 0f..1f,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            AppCardWithContent(
                label = stringResource(R.string.widget_theme)
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s)
                ) {
                    val themes = remember {
                        WidgetTheme.entries.let {
                            if (SDK_INT < 31) {
                                it.subtract(setOf(WidgetTheme.DYNAMIC))
                            } else it
                        }
                    }
                    themes.forEach { widgetTheme ->
                        AppChip(
                            selected = widgetTheme == options.widgetTheme,
                            onClick = { options = options.copy(widgetTheme = widgetTheme) },
                            label = stringResource(widgetTheme.nameResId)
                        )
                    }
                }
            }
            AppCardWithContent(
                label = stringResource(R.string.widget_elements_color)
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s)
                ) {
                    WidgetTextColor.entries.forEach { textColor ->
                        AppChip(
                            selected = textColor == options.elementsColor,
                            onClick = { options = options.copy(elementsColor = textColor) },
                            label = stringResource(textColor.nameResId)
                        )
                    }
                }
            }
            AppCardWithContent(
                label = stringResource(R.string.widget_text_color)
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s)
                ) {
                    WidgetTextColor.entries.forEach { textColor ->
                        AppChip(
                            selected = textColor == options.textColor,
                            onClick = { options = options.copy(textColor = textColor) },
                            label = stringResource(textColor.nameResId)
                        )
                    }
                }
            }
            Spacer(Modifier.height(paddingValues.calculateBottomPadding()))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WidgetSettingsTopBar(
    widgetOptions: WidgetOptions,
    colors: WidgetColors,
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
        WidgetPreview(
            options = widgetOptions,
            colors = colors,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .drawWithContent {
                    drawRect(
                        color = Color.Transparent,
                        blendMode = BlendMode.Clear
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