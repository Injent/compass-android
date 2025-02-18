package ru.bgitu.feature.schedule_widget.widget.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import ru.bgitu.core.designsystem.util.shadow.toDp
import ru.bgitu.feature.schedule_widget.createImageProvider

@Composable
fun CustomScaffold(
    modifier: GlanceModifier = GlanceModifier,
    backgroundColor: Color,
    backgroundAlpha: Float,
    titleBar: @Composable (() -> Unit)? = null,
    horizontalPadding: Dp = 6.dp,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val size = LocalSize.current

    val cornerRadius = if (android.os.Build.VERSION.SDK_INT >= 31) {
        val systemCornerRadiusDefined = LocalContext.current.resources
            .getResourceName(android.R.dimen.system_app_widget_background_radius) != null

        if (systemCornerRadiusDefined) {
            context.resources.getDimension(android.R.dimen.system_app_widget_background_radius).toDp.dp
        } else 16.dp
    } else 16.dp

    val theModifier = modifier
        .fillMaxSize()
        .background(
            run {
                createImageProvider(
                    size = size,
                    cornerRadius = cornerRadius.value.toInt(),
                    color = backgroundColor,
                    alpha = backgroundAlpha
                )
            }
        )
        .appWidgetBackground()
        .cornerRadius(cornerRadius)

    Box(
        modifier = theModifier
    ) {
        Column(GlanceModifier.fillMaxSize()) {
            titleBar?.invoke()
            Box(
                modifier = GlanceModifier.padding(horizontal = horizontalPadding).defaultWeight(),
                content = content
            )
        }
    }
}