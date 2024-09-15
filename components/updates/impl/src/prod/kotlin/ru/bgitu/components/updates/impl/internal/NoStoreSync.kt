package ru.bgitu.components.updates.impl.internal

import android.content.Context
import ru.bgitu.components.updates.api.StoreSync

class NoStoreSync : StoreSync {
    override fun sync(context: Context) {}
}