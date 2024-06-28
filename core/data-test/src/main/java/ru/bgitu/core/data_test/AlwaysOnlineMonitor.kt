package ru.bgitu.core.data_test

import kotlinx.coroutines.flow.flowOf
import ru.bgitu.core.data.util.NetworkMonitor

class AlwaysOnlineMonitor : NetworkMonitor {
    override val isOnline = flowOf(true)
}