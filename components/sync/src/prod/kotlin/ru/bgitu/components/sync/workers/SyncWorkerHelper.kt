package ru.bgitu.components.sync.workers

import androidx.work.Constraints
import androidx.work.NetworkType

internal val SyncWorkerConstraints get() = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .build()