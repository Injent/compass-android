package ru.bgitu.components.updates.impl

import android.content.Context
import androidx.appcompat.app.AlertDialog
import ru.bgitu.components.updates.api.StoreSync
import ru.rustore.sdk.core.util.RuStoreUtils

class RuStoreSync : StoreSync {
    override fun sync(context: Context) {
        if (!RuStoreUtils.isRuStoreInstalled(context)) {
            context.createAlertDialog().show()
        }
    }

    private fun Context.createAlertDialog(): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle(R.string.rustore_dialog_title)
            .setMessage(R.string.rustore_dialog_message)
            .setPositiveButton(R.string.rustore_dialog_action_download) { _, _ ->
                RuStoreUtils.openRuStoreDownloadInstruction(this)
            }
            .setCancelable(false)
            .create()
    }
}