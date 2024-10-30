package ru.bgitu.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.bgitu.core.designsystem.components.AppSnackbarTokens.MaxHeight
import ru.bgitu.core.designsystem.components.AppSnackbarTokens.MaxWidth
import ru.bgitu.core.designsystem.components.AppSnackbarTokens.MinHeight
import ru.bgitu.core.designsystem.components.AppSnackbarTokens.OuterPadding
import ru.bgitu.core.designsystem.theme.AppTheme
import java.time.temporal.ChronoUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toKotlinDuration

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
    error("Snackbar controller not provided")
}

@Composable
fun rememberSnackbarController(): SnackbarController {
    val coroutineScope = rememberCoroutineScope()
    return remember { ru.bgitu.core.designsystem.components.SnackbarController(coroutineScope) }
}

data class SnackbarState(
    val content: @Composable () -> Unit
)

@Stable
class SnackbarController(
    private val coroutineScope: CoroutineScope
) {
    private var snackbarStayJob: Job? = null
    var snackbarState: SnackbarState? by mutableStateOf(null)
        private set

    fun show(
        duration: Duration = IndefiniteDuration,
        message: String,
        withDismissAction: Boolean,
        @DrawableRes icon: Int,
        actionLabel: String? = null,
        onAction: () -> Unit = {}
    ) {
        runSnackbarStayJob(duration)
        snackbarState = SnackbarState(
            content = {
                DefaultSnackbar(
                    text = message,
                    icon = {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    },
                    withDismissAction = withDismissAction,
                    onDismissRequest = ::dismiss,
                    onAction = onAction,
                    actionLabel = actionLabel
                )
            }
        )
    }

    fun mutableShow(
        duration: Duration = IndefiniteDuration,
        message: State<String>,
        withDismissAction: State<Boolean>,
        icon: State<Int>,
        iconTint: State<Color>,
        actionLabel: State<String?>,
        onAction: () -> Unit = {}
    ) {
        runSnackbarStayJob(duration)
        snackbarState = SnackbarState(
            content = {
                DefaultSnackbar(
                    text = message.value,
                    icon = {
                        Icon(
                            painter = painterResource(icon.value),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = iconTint.value
                        )
                    },
                    withDismissAction = withDismissAction.value,
                    onDismissRequest = ::dismiss,
                    onAction = onAction,
                    actionLabel = actionLabel.value
                )
            }
        )
    }

    fun dismiss() {
        if (snackbarStayJob?.isActive == true) {
            snackbarStayJob?.cancel()
        }
        snackbarState = null
    }

    private fun runSnackbarStayJob(duration: Duration) {
        snackbarStayJob = coroutineScope.launch {
            delay(duration)
            if (coroutineContext.isActive) {
                snackbarState = null
            }
        }
    }

    companion object {
        private val IndefiniteDuration = ChronoUnit.FOREVER.duration.toKotlinDuration()
    }
}

@Composable
fun AppSnackbarHost(
    modifier: Modifier = Modifier
) {
    val snackbarState = LocalSnackbarController.current.snackbarState

    var oldSnackbarState: SnackbarState? by remember { mutableStateOf(snackbarState) }

    LaunchedEffect(snackbarState) {
        if (snackbarState != null) {
            oldSnackbarState = snackbarState
        }
        delay(1.seconds)
        oldSnackbarState = snackbarState
    }

    val yOffset by animateFloatAsState(
        targetValue = if (snackbarState != null) {
            0f
        } else 600f,
        animationSpec = tween(durationMillis = 400),
        label = ""
    )

    oldSnackbarState?.let {
        AppSnackbarContainer(
            content = it.content,
            modifier = modifier
                .widthIn(max = 400.dp)
                .zIndex(2f)
                .offset {
                    IntOffset(0, yOffset.toInt())
                },
        )
    }
}

@Composable
private fun AppSnackbarContainer(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = AppSnackbarTokens.ContentPadding,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = MaxWidth)
            .heightIn(min = MinHeight, max = MaxHeight)
            .padding(OuterPadding),
        color = AppTheme.colorScheme.backgroundToast,
        shape = AppTheme.shapes.default,
    ) {
        Box(Modifier.padding(contentPadding)) {
            content()
        }
    }
}

private object AppSnackbarTokens {
    val MaxWidth = 344.dp
    val MinHeight = 52.dp
    val MaxHeight = 82.dp
    val ContentPadding = PaddingValues(16.dp)
    val OuterPadding = PaddingValues(8.dp)
}

@Composable
private fun DefaultSnackbar(
    text: String,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
    actionLabel: String? = null,
    onAction: () -> Unit,
    withDismissAction: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
        modifier = modifier
    ) {
        Box(
            Modifier
                .align(Alignment.Top)
                .size(20.dp)
        ) {
            icon()
        }

        Text(
            text = text,
            style = AppTheme.typography.footnote,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )

        actionLabel?.let {
            Text(
                text = it,
                style = AppTheme.typography.subheadlineButton,
                color = AppTheme.colorScheme.foregroundWarning,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .pointerInput(onAction) {
                        detectTapGestures(
                            onTap = {
                                onAction()
                            }
                        )
                    }
            )
        }

        if (withDismissAction) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                tint = AppTheme.colorScheme.foreground3,
                modifier = Modifier
                    .align(Alignment.Top)
                    .size(20.dp)
                    .pointerInput(onDismissRequest) {
                        detectTapGestures {
                            onDismissRequest()
                        }
                    }
            )
        }
    }
}