package ru.bgitu.core.common

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
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
