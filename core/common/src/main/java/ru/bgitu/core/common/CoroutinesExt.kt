package ru.bgitu.core.common

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

inline fun <reified T> eventChannel(): Channel<T> = Channel(Channel.CONFLATED)