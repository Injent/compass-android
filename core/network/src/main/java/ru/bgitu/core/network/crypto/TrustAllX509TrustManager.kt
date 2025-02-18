package ru.bgitu.core.network.crypto

import android.annotation.SuppressLint
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

@SuppressLint("TrustAllX509TrustManager")
class TrustAllX509TrustManager : X509TrustManager {
    override fun checkClientTrusted(certificates: Array<out X509Certificate>?, authType: String?) {}
    override fun checkServerTrusted(certificates: Array<out X509Certificate>?, authType: String?) {}
    override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)
}