package ru.bgitu.core.ui

import android.view.ContextThemeWrapper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import ru.bgitu.core.designsystem.R
import ru.bgitu.core.designsystem.components.DynamicAsyncImage
import ru.bgitu.core.designsystem.theme.AppTheme

@Composable
fun ProfileImage(
    avatarUrl: String?,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = AppTheme.isDarkTheme
    val context = LocalContext.current
    val painter = remember {
        val wrapper = ContextThemeWrapper(
            context,
            if (isDarkTheme) R.style.AvatarPlaceholderNight else R.style.AvatarPlaceholder
        )
        val bitmap = ResourcesCompat.getDrawable(
            context.resources,
            R.drawable.avatar_placeholder,
            wrapper.theme
        )?.toBitmap()
        bitmap?.asImageBitmap()?.let { BitmapPainter(image = it) }
    }

    DynamicAsyncImage(
        imageUrl = avatarUrl,
        placeholder = {
            Image(
                painter = checkNotNull(painter) { "Resource is modified or theme is invalid" },
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        },
        modifier = modifier.defaultMinSize(minHeight = 48.dp, minWidth = 48.dp),
    )
}