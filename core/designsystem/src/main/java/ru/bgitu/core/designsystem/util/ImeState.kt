package ru.bgitu.core.designsystem.util

import android.app.Activity
import android.graphics.Rect
import android.os.Build.VERSION.SDK_INT
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Based on the following Stackoverflow answer:
 * http://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android
 */
class KeyboardUtils private constructor(
    activity: Activity, listener: SoftKeyboardToggleListener
) : OnGlobalLayoutListener {
    private var callback: SoftKeyboardToggleListener?
    private val rootView: View
    private var prevValue: Boolean? = null
    private val screenDensity: Float

    fun interface SoftKeyboardToggleListener {
        fun onToggleSoftKeyboard(isVisible: Boolean)
    }


    override fun onGlobalLayout() {
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)

        val heightDiff = rootView.rootView.height - (r.bottom - r.top)
        val dp = heightDiff / screenDensity
        val isVisible = dp > MAGIC_NUMBER

        if (callback != null && (prevValue == null || isVisible != prevValue)) {
            prevValue = isVisible
            callback!!.onToggleSoftKeyboard(isVisible)
        }
    }

    private fun removeListener() {
        callback = null

        rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    init {
        callback = listener

        rootView = (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(this)

        screenDensity = activity.resources.displayMetrics.density
    }

    companion object {
        private const val MAGIC_NUMBER = 200

        private val listeners = HashMap<SoftKeyboardToggleListener, KeyboardUtils>()

        /**
         * Add a new keyboard listener
         * @param activity calling activity
         * @param listener callback
         */
        fun addKeyboardToggleListener(activity: Activity, listener: SoftKeyboardToggleListener) {
            removeKeyboardToggleListener(listener)

            listeners[listener] = KeyboardUtils(activity, listener)
        }

        /**
         * Remove a registered listener
         * @param listener [SoftKeyboardToggleListener]
         */
        fun removeKeyboardToggleListener(listener: SoftKeyboardToggleListener) {
            if (listeners.containsKey(listener)) {
                val k = listeners[listener]
                k!!.removeListener()

                listeners.remove(listener)
            }
        }
    }
}

@Composable
fun rememberImeState(): State<Boolean> {
    val isImeVisible = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val view = LocalView.current
    val viewTreeObserver = view.viewTreeObserver
    DisposableEffect(viewTreeObserver) {
        if (SDK_INT <= 31) {
            val listener = KeyboardUtils.SoftKeyboardToggleListener {
                isImeVisible.value = it
            }
            KeyboardUtils.addKeyboardToggleListener(
                activity = context as Activity,
                listener = listener
            )

            onDispose {
                KeyboardUtils.removeKeyboardToggleListener(listener)
            }
        } else {
            val listener = OnGlobalLayoutListener {
                isImeVisible.value = ViewCompat.getRootWindowInsets(view)
                    ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            }

            viewTreeObserver.addOnGlobalLayoutListener(listener)
            onDispose {
                viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }
        }
    }

    return isImeVisible
}