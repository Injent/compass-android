package ru.bgitu.core.common

import androidx.annotation.StringRes

sealed class TextResource {
    data class Id(@StringRes val resId: Int) : TextResource()
    data class Plain(val plain: String) : TextResource()
    class DynamicString(@StringRes val resId: Int, vararg val args: String) : TextResource()
}
