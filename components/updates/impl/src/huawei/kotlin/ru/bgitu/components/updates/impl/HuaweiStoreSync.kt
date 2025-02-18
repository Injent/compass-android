package ru.bgitu.components.updates.impl

import android.app.Activity
import android.content.Context
import com.huawei.hms.adapter.AvailableAdapter
import com.huawei.hms.adapter.internal.AvailableCode
import ru.bgitu.components.updates.api.StoreSync


private const val MAGIC_NUMBER_FROM_HUAWEI_DOCS = 60400312

class HuaweiStoreSync : StoreSync {
    override fun sync(context: Context) {
        if (context !is Activity) return
        val availableAdapter = AvailableAdapter(MAGIC_NUMBER_FROM_HUAWEI_DOCS)
        val result = availableAdapter.isHuaweiMobileServicesAvailable(context)

        if (result != AvailableCode.SUCCESS) {
            availableAdapter.startResolution(
                context
            ) {
            }
        }
    }
}