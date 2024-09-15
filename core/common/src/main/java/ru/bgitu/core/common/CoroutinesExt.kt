package ru.bgitu.core.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

inline fun <reified T> eventChannel(): Channel<T> = Channel(Channel.CONFLATED)

fun Job?.relaunch(
    scope: CoroutineScope,
    block: suspend CoroutineScope.() -> Unit
): Job {
    this?.cancel()
    return scope.launch(block = block)
}