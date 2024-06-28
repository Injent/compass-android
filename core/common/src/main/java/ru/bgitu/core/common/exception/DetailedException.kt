package ru.bgitu.core.common.exception

import ru.bgitu.core.common.TextResource
import java.io.IOException

class DetailedException(
    val details: TextResource,
    val originalException: Throwable? = null
) : IOException() {
    override fun printStackTrace() {
        originalException?.printStackTrace()
    }
}