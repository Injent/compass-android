package ru.bgitu.core.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import android.provider.Settings
import androidx.core.net.toUri
import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

val Context.versionName: String
    get() {
        return if (SDK_INT >= 33) {
            this.packageManager.getPackageInfo(
                this.packageName, PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            this.packageManager.getPackageInfo(this.packageName, 0)
        }.versionName
    }

val Context.versionCode: Long
    get() {
        return if (SDK_INT >= 33) {
            this.packageManager.getPackageInfo(
                this.packageName,
                PackageManager.PackageInfoFlags.of(0)
            ).longVersionCode
        } else if (SDK_INT >= 28) {
            this.packageManager.getPackageInfo(this.packageName, 0).longVersionCode
        } else {
            @Suppress("DEPRECATION")
            this.packageManager.getPackageInfo(this.packageName, 0).versionCode.toLong()
        }
    }

private var _animationDurationScale: Float? = null

val Context.animationDurationScale: Float
    get() = if (_animationDurationScale == null) {
        Settings.Global.getFloat(contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f).also {
            _animationDurationScale = it
        }
    } else _animationDurationScale!!

fun <T> weakReference(tIn: T? = null): ReadWriteProperty<Any?, T?> {
    return object : ReadWriteProperty<Any?, T?> {
        var t = WeakReference<T?>(tIn)
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return t.get()
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            t = WeakReference(value)
        }
    }
}

fun Context.openUrl(url: String) {
    this.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}

enum class ScreenRotation {
    LEFT,
    NONE,
    RIGHT
}

@Suppress("DEPRECATION")
val Context.screenRotation: ScreenRotation
    get() {
        val rotation = if (SDK_INT > 30) {
            (this as Activity).display?.rotation ?: 0
        } else (this as Activity).windowManager.defaultDisplay.rotation

        return when (rotation) {
            1 -> ScreenRotation.LEFT
            3 -> ScreenRotation.RIGHT
            else -> ScreenRotation.NONE
        }
    }