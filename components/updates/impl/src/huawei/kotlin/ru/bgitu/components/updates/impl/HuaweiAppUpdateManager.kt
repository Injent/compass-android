package ru.bgitu.components.updates.impl

import android.content.Context
import android.content.Intent
import android.os.Build
import com.huawei.hms.jos.AppUpdateClient
import com.huawei.hms.jos.JosApps
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack
import com.huawei.updatesdk.service.otaupdate.UpdateKey
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import ru.bgitu.components.updates.api.AppUpdateManager
import ru.bgitu.components.updates.api.model.InstallState
import ru.bgitu.components.updates.api.model.UnknownUpdateInfo
import ru.bgitu.components.updates.api.model.UpdateInfo
import ru.bgitu.core.common.Result
import java.io.Serializable

class HuaweiAppUpdateManager(
    context: Context
) : AppUpdateManager {
    override val appUpdateInfo: Flow<UpdateInfo> = callbackFlow<UpdateInfo> {
        val client: AppUpdateClient = JosApps.getAppUpdateClient(context)

        client.checkAppUpdate(
            context,
            object : CheckUpdateCallBack {
                override fun onUpdateInfo(intent: Intent) {
                    val forceUpdate = intent.getBooleanExtra(UpdateKey.MUST_UPDATE, false)
                    val info = intent.serializable<ApkUpgradeInfo>(UpdateKey.INFO)

                    if (info != null) {
                        client.showUpdateDialog(context, info, forceUpdate)
                    }
                    trySend(UnknownUpdateInfo)
                }

                override fun onMarketInstallInfo(intent: Intent) {
                }

                override fun onMarketStoreError(error: Int) {
                }

                override fun onUpdateStoreError(error: Int) {
                }
            }
        )

        awaitClose {}
    }
        .catch { e -> e.printStackTrace() }
        .onStart {
            emit(UnknownUpdateInfo)
        }

    override val installState: Flow<InstallState> = flowOf(InstallState.Unknown)

    override fun startUpdateFlow(updateInfo: UpdateInfo) {}

    override suspend fun completeUpdate(): Result<Unit> {
        return Result.Success(Unit)
    }

    override fun safelyClearUpdates() {}
}

private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}