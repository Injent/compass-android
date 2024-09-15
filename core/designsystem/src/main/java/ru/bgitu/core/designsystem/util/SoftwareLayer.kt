package ru.bgitu.core.designsystem.util

import android.os.Build.VERSION.SDK_INT
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun SoftwareLayer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (SDK_INT < 28) {
        AndroidView(
            factory = { context ->
                ComposeView(context).apply {
                    setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                }
            },
            update = { composeView ->
                (composeView.parent as? ViewGroup)?.clipChildren = false
                composeView.setContent(content)
            },
            modifier = modifier
        )
    } else {
        Box(modifier = modifier, content = { content() })
    }
}