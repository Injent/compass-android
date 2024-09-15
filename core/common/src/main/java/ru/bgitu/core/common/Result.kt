package ru.bgitu.core.common

import ru.bgitu.core.common.exception.DetailedException
import java.nio.channels.UnresolvedAddressException

/**
 * The class is used to safely receive data and handle possible exceptions.
 * This is used in data layer (repositories, use-cases, utils).
 */
sealed class Result<out T> private constructor() {

    /**
     * The class indicating the success of the operation and contains it's data.
     */
    data class Success<out R>(val value: R) : Result<R>()

    /**
     * The class indicating the failure of an operation and contains an exception and localized
     * error text.
     *
     * @param details localized error information.
     * @param throwable exception that caused the operation to be interrupted.
     */
    data class Failure(
        val throwable: Throwable? = null,
        val details: TextResource = TextResource.Id(R.string.error_unknown),
    ) : Result<Nothing>()

    /**
     * @return `true` if the result of the operation was successful.
     */
    val isSuccess: Boolean
        get() = this is Success


    /**
     * Calls a block of code in the parameter if the result of the operation was successful.
     *
     * @param block the block of code that should be called when the operation is successful.
     */
    suspend fun onSuccess(block: suspend (T) -> Unit): Result<T> {
        if (this is Success) {
            block(this.value)
        }
        return this
    }

    /**
     * Calls a block of code in the parameter if the result of the operation was unsuccessful.
     *
     * @param block the block of code that should be called when the operation is unsuccessful.
     */
    suspend fun onFailure(block: suspend (Failure) -> Unit): Result<T> {
        if (this is Failure) {
            block(this)
        }
        return this
    }
}

/**
 * A mapper function that transforms the raw result of an operation into a handled result.
 *
 * @param block a block of code that is responsible for receiving data with the ability to throw an
 * exception.
 *
 * @return [Result.Success] if the data was successfully received and processed or
 * [Result.Failure] if an exception was thrown when performing the operation.
 */
inline fun <reified T> runResulting(
    block: () -> T
): Result<T> {
    return runCatching {
        Result.Success(block())
    }.getOrElse { e ->
        e.printStackTrace()
        Result.Failure(
            throwable = e,
            details = when (e) {
                is DetailedException -> e.details
                is UnresolvedAddressException -> TextResource.Id(CommonStrings.error_no_internet_connection)
                else -> TextResource.Id(R.string.error_unknown)
            },
        )
    }
}

/**
 * Transforms the result of the operation according to the instructions.
 *
 * @param transform block of code with instructions for transforming the result.
 * @return a transformed result wrapped in a [Result]
 */
@JvmName("map")
inline fun <T, reified R> Result<T>.map(
    crossinline transform: (value: T) -> R
): Result<R> {
    return when (this) {
        is Result.Failure -> {
            Result.Failure(throwable = throwable, details = details)
        }
        is Result.Success -> {
            Result.Success(transform(this.value))
        }
    }
}

@JvmName("mapList")
inline fun <T, reified R> Result<List<T>>.map(
    crossinline transform: (value: T) -> R
): Result<List<R>> {
    return when (this) {
        is Result.Failure -> {
            Result.Failure(throwable = throwable, details = details)
        }
        is Result.Success -> {
            Result.Success(value.map(transform))
        }
    }
}


/**
 * @return a successful data that contains in [Result.Success] or invokes a block of code if
 * the result is [Result.Failure]
 */
inline fun <R, T : R> Result<T>.getOrElse(onFailure: (Result.Failure) -> R): R {
    return when (this) {
        is Result.Success -> value
        is Result.Failure -> onFailure(this)
    }
}

/**
 * @return a successful data that contains in [Result.Success] or throws an exception that
 * contains in [Result.Failure]
 */
fun <R, T : R> Result<T>.getOrThrow(): R {
    return when (this) {
        is Result.Success -> value
        is Result.Failure -> throw this.throwable
            ?: DetailedException(TextResource.Id(ru.bgitu.core.common.R.string.error_unknown))
    }
}

/**
 * @return a successful data that contains in [Result.Success] or `null` if result is
 * [Result.Failure]
 */
inline fun <reified T> Result<T>.getOrNull(): T? {
    if (this is Result.Success)
        return this.value
    return null
}
