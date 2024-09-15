package ru.bgitu.components.updates.impl.model

import ru.bgitu.components.updates.api.model.UpdateInfo
import ru.rustore.sdk.appupdate.model.AppUpdateInfo

data class RuStoreUpdateInfo(
    val data: AppUpdateInfo
) : UpdateInfo