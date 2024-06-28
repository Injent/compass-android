package ru.bgitu.core.common

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.core.net.toUri
import java.util.Locale

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

fun isHuaweiDevice(): Boolean {
    val manufacturer = Build.MANUFACTURER
    val brand = Build.BRAND
    return manufacturer.lowercase(Locale.getDefault()).contains("huawei") || brand.lowercase(
        Locale.ENGLISH
    ).contains("huawei")
}

fun Context.openUrl(url: String) {
    this.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}
