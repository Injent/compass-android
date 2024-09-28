package ru.bgitu.feature.schedule_widget.widget.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import ru.bgitu.feature.schedule_widget.R

@Composable
fun CustomScaffold(
    modifier: GlanceModifier = GlanceModifier,
    background: ImageProvider,
    titleBar: @Composable (() -> Unit)? = null,
    horizontalPadding: Dp = 6.dp,
    content: @Composable () -> Unit,
) {
    var theModifier = modifier
        .fillMaxSize()
        .background(background)
        .appWidgetBackground()

    if (android.os.Build.VERSION.SDK_INT >= 31) {
        val systemCornerRadiusDefined = LocalContext.current.resources
            .getResourceName(android.R.dimen.system_app_widget_background_radius) != null

        theModifier = if (systemCornerRadiusDefined) {
            theModifier.cornerRadius(android.R.dimen.system_app_widget_background_radius)
        } else {
            theModifier.cornerRadius(16.dp)
        }
    }

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