package ru.bgitu.core.data.migrations

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.core.content.edit
import ru.bgitu.core.data.R
import java.io.File

object CriticalChangesService {
    private const val SHARED_PREFS_NAME = "markers"
    private const val KEY_CRITICAL_UPDATES_VERSION = "critical_updates"

    fun fix(activity: Activity, criticalVersionsStringArray: String) {
        val criticalVersionsInt = criticalVersionsStringArray
            .split(';')
            .map(String::toInt)
        val criticalVersions = criticalVersionsInt.map { it.toString() }.toSet()

        val prefs = activity.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        if (
            File(activity.applicationInfo.dataDir, "shared_prefs/$SHARED_PREFS_NAME")
                .exists().not()
        ) {
            prefs.edit(commit = true) {
                putStringSet(KEY_CRITICAL_UPDATES_VERSION, criticalVersions)
            }
            return
        }

        val criticalUpdatesVersion = prefs.getStringSet(KEY_CRITICAL_UPDATES_VERSION, emptySet())!!

        if (criticalUpdatesVersion.containsAll(criticalVersions)) return

        File(activity.applicationInfo.dataDir).deleteRecursively()

        prefs.edit(commit = true) {
            putStringSet(KEY_CRITICAL_UPDATES_VERSION, criticalVersions)
        }

        Toast.makeText(activity, R.string.toast_finishing_update, Toast.LENGTH_SHORT).show()
        activity.recreate()
    }
}