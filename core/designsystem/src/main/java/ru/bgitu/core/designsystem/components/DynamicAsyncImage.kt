package ru.bgitu.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter.State.Error
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter
import ru.bgitu.core.designsystem.util.shimmer
import ru.bgitu.core.designsystem.util.thenIf

@Composable
fun DynamicAsyncImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    placeholder: (@Composable () -> Unit)? = null,
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val isEmpty = remember(imageUrl) { imageUrl.isNullOrEmpty() }
    val imageLoader = rememberAsyncImagePainter(
        model = imageUrl,
        onState = { state ->
            isLoading = state is Loading
            isError = state is Error
        },
    )

    Box(
        modifier = modifier
            .thenIf(isLoading) {
                Modifier.shimmer()
            },
        contentAlignment = Alignment.Center,
    ) {
        if (isEmpty || isError) {
            placeholder?.let {
                Box(modifier = Modifier.matchParentSize()) {
                    it()
                }
            }
        } else {
            Image(
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                painter = imageLoader,
                contentDescription = contentDescription
            )
        }
    }
}