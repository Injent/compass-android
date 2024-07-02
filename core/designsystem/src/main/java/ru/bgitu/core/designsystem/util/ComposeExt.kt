package ru.bgitu.core.designsystem.util

import android.content.Context
import android.view.ViewTreeObserver
import androidx.annotation.RawRes
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.MotionScene
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import ru.bgitu.core.common.TextResource
import kotlin.math.max

@Composable
fun rememberMotionScene(
    @RawRes resId: Int,
    params: Map<String, Int>? = null
): MotionScene {
    val context = LocalContext.current
    return MotionScene(
        remember(resId, params) {
            val sb = StringBuilder(context.resources.openRawResource(resId).readBytes().decodeToString())

            params?.let {
                for ((name, value) in it) {
                    var boolEnded = 0
                    while (boolEnded != -1) {
                        if (boolEnded != 0) {
                            sb.replace(boolEnded, boolEnded + name.length + 2, value.toString())
                        }
                        boolEnded = sb.indexOf("\'$name\'")
                    }
                }
            }

            sb.toString()
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun adaptiveDp(
    compact: Dp,
    medium: Dp,
    expanded: Dp
): Dp {
    val width = currentWindowAdaptiveInfo().windowSizeClass.widthSizeClass
    return when {
        width >= WindowWidthSizeClass.Expanded -> expanded
        width >= WindowWidthSizeClass.Medium -> medium
        else -> compact
    }
}

val TextUnit.nonScaledSp: TextUnit
    @Composable
    get() = (this.value / LocalDensity.current.fontScale).sp

@Composable
fun TextResource.asString(): String {
    return when (this) {
        is TextResource.Id -> stringResource(resId)
        is TextResource.Plain -> plain
        is TextResource.DynamicString -> stringResource(resId, *args)
    }
}

fun TextResource.asString(context: Context): String {
    return when (this) {
        is TextResource.Id -> context.getString(resId)
        is TextResource.Plain -> plain
        is TextResource.DynamicString -> context.getString(resId, *args)
    }
}

class ImeAnimationState {
    var translationY by mutableFloatStateOf(0f)
        internal set
    var screenHeight by mutableIntStateOf(0)
        internal set
    var fraction by mutableFloatStateOf(1f)
        internal set
}

@Composable
fun rememberImeAnimationState(): ImeAnimationState {
    val imeAnimationState by remember {
        mutableStateOf(ImeAnimationState())
    }

    val view = LocalView.current
    val viewTreeObserver = view.viewTreeObserver

    DisposableEffect(viewTreeObserver) {
        ViewCompat.setWindowInsetsAnimationCallback(
            view.rootView,
            object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
                private var startBottom = 0
                private var endBottom = 0
                private var imeVisible = false

                override fun onPrepare(animation: WindowInsetsAnimationCompat) {
                    super.onPrepare(animation)
                    startBottom = view.bottom
                    imeAnimationState.screenHeight = view.rootView.bottom
                    imeVisible = ViewCompat.getRootWindowInsets(view)
                        ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
                }

                override fun onStart(
                    animation: WindowInsetsAnimationCompat,
                    bounds: WindowInsetsAnimationCompat.BoundsCompat
                ): WindowInsetsAnimationCompat.BoundsCompat {
                    endBottom = view.bottom
                    return bounds
                }

                override fun onProgress(
                    insets: WindowInsetsCompat,
                    runningAnimations: MutableList<WindowInsetsAnimationCompat>
                ): WindowInsetsCompat {
                    val imeAnimation = runningAnimations.find {
                        it.typeMask and WindowInsetsCompat.Type.ime() != 0
                    } ?: return insets

                    imeAnimationState.fraction = if (imeVisible) {
                        imeAnimation.interpolatedFraction
                    } else 1 - imeAnimation.interpolatedFraction

                    imeAnimationState.translationY = (startBottom - endBottom) * (1 - imeAnimation.interpolatedFraction)
                    return insets
                }
            }
        )

        onDispose {
            ViewCompat.setWindowInsetsAnimationCallback(view, null)
        }
    }

    return imeAnimationState
}

@Composable
inline fun Modifier.thenIf(condition: Boolean, block: @Composable Modifier.() -> Modifier): Modifier {
    return if (condition) then(block()) else this
}

fun Modifier.scrollbar(
    state: ScrollState,
    direction: Orientation,
    indicatorThickness: Dp = 8.dp,
    indicatorColor: Color = Color.LightGray,
    alpha: Float = if (state.isScrollInProgress) 0.8f else 0f,
    alphaAnimationSpec: AnimationSpec<Float> = tween(
        delayMillis = if (state.isScrollInProgress) 0 else 1500,
        durationMillis = if (state.isScrollInProgress) 150 else 500
    ),
    padding: PaddingValues = PaddingValues(all = 0.dp)
): Modifier = composed {
    val scrollbarAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = alphaAnimationSpec,
        label = ""
    )

    drawWithContent {
        drawContent()

        val showScrollBar = state.isScrollInProgress || scrollbarAlpha > 0.0f

        // Draw scrollbar only if currently scrolling or if scroll animation is ongoing.
        if (showScrollBar) {
            val (topPadding, bottomPadding, startPadding, endPadding) = listOf(
                padding.calculateTopPadding().toPx(), padding.calculateBottomPadding().toPx(),
                padding.calculateStartPadding(layoutDirection).toPx(),
                padding.calculateEndPadding(layoutDirection).toPx()
            )
            val contentOffset = state.value
            val viewPortLength = if (direction == Orientation.Vertical)
                size.height else size.width
            val viewPortCrossAxisLength = if (direction == Orientation.Vertical)
                size.width else size.height
            val contentLength = max(viewPortLength + state.maxValue, 0.001f)  // To prevent divide by zero error
            val indicatorLength = ((viewPortLength / contentLength) * viewPortLength) - (
                    if (direction == Orientation.Vertical) topPadding + bottomPadding
                    else startPadding + endPadding
                    )
            val indicatorThicknessPx = indicatorThickness.toPx()

            val scrollOffsetViewPort = viewPortLength * contentOffset / contentLength

            val scrollbarSizeWithoutInsets = if (direction == Orientation.Vertical)
                Size(indicatorThicknessPx, indicatorLength)
            else Size(indicatorLength, indicatorThicknessPx)

            val scrollbarPositionWithoutInsets = if (direction == Orientation.Vertical)
                Offset(
                    x = if (layoutDirection == LayoutDirection.Ltr)
                        viewPortCrossAxisLength - indicatorThicknessPx - endPadding
                    else startPadding,
                    y = scrollOffsetViewPort + topPadding
                )
            else
                Offset(
                    x = if (layoutDirection == LayoutDirection.Ltr)
                        scrollOffsetViewPort + startPadding
                    else viewPortLength - scrollOffsetViewPort - indicatorLength - endPadding,
                    y = viewPortCrossAxisLength - indicatorThicknessPx - bottomPadding
                )

            drawRoundRect(
                color = indicatorColor,
                cornerRadius = CornerRadius(
                    x = indicatorThicknessPx / 2, y = indicatorThicknessPx / 2
                ),
                topLeft = scrollbarPositionWithoutInsets,
                size = scrollbarSizeWithoutInsets,
                alpha = scrollbarAlpha
            )
        }
    }
}

data class ScrollBarConfig(
    val indicatorThickness: Dp = 8.dp,
    val indicatorColor: Color = Color.LightGray,
    val alpha: Float? = null,
    val alphaAnimationSpec: AnimationSpec<Float>? = null,
    val padding: PaddingValues = PaddingValues(all = 0.dp)
)

fun Modifier.verticalScrollWithScrollbar(
    state: ScrollState,
    enabled: Boolean = true,
    flingBehavior: FlingBehavior? = null,
    reverseScrolling: Boolean = false,
    scrollbarConfig: ScrollBarConfig = ScrollBarConfig()
) = this
    .scrollbar(
        state, Orientation.Vertical,
        indicatorThickness = scrollbarConfig.indicatorThickness,
        indicatorColor = scrollbarConfig.indicatorColor,
        alpha = scrollbarConfig.alpha ?: if (state.isScrollInProgress) 0.8f else 0f,
        alphaAnimationSpec = scrollbarConfig.alphaAnimationSpec ?: tween(
            delayMillis = if (state.isScrollInProgress) 0 else 1500,
            durationMillis = if (state.isScrollInProgress) 150 else 500
        ),
        padding = scrollbarConfig.padding
    )
    .verticalScroll(state, enabled, flingBehavior, reverseScrolling)



fun Modifier.horizontalScrollWithScrollbar(
    state: ScrollState,
    enabled: Boolean = true,
    flingBehavior: FlingBehavior? = null,
    reverseScrolling: Boolean = false,
    scrollbarConfig: ScrollBarConfig = ScrollBarConfig()
) = this
    .scrollbar(
        state, Orientation.Horizontal,
        indicatorThickness = scrollbarConfig.indicatorThickness,
        indicatorColor = scrollbarConfig.indicatorColor,
        alpha = scrollbarConfig.alpha ?: if (state.isScrollInProgress) 0.8f else 0f,
        alphaAnimationSpec = scrollbarConfig.alphaAnimationSpec ?: tween(
            delayMillis = if (state.isScrollInProgress) 0 else 1500,
            durationMillis = if (state.isScrollInProgress) 150 else 500
        ),
        padding = scrollbarConfig.padding
    )
    .horizontalScroll(state, enabled, flingBehavior, reverseScrolling)

@Composable
fun ClearFocusWithImeEffect(
    block: ((visible: Boolean) -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    val isImeVisible by rememberImeState()

    LaunchedEffect(isImeVisible) {
        block?.invoke(isImeVisible)
        if (isImeVisible) return@LaunchedEffect
        focusManager.clearFocus()
    }
}

fun TextFieldState.textAsFlow(): Flow<String> {
    return snapshotFlow { this.text }.mapLatest(CharSequence::toString)
}