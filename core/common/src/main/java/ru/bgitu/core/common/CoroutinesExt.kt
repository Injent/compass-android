package ru.bgitu.core.common

import kotlinx.coroutines.channels.Channel

inline fun <reified T> eventChannel(): Channel<T> = Channel(Channel.CONFLATED)