package ru.bgitu.components.sitetraffic

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import kotlin.random.Random

@SuppressLint("SetJavaScriptEnabled")
class TrafficWebView(context: Context) : WebView(context) {

    private val siteLinks = mapOf(
        "https://bgitu.ru/studentu/raspisanie/ochnoe-obuchenie" to 60,
        "https://bgitu.ru/studentu/raspisanie" to 40,
    )

    init {
        with(settings) {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            domStorageEnabled = true
            databaseEnabled = false
            builtInZoomControls = false
            displayZoomControls = false
            allowFileAccess = false
            allowContentAccess = false
            blockNetworkImage = true
            setGeolocationEnabled(false)
            javaScriptCanOpenWindowsAutomatically = false
            setSupportMultipleWindows(false)
            setSupportZoom(false)
            mediaPlaybackRequiresUserGesture = true

            isHorizontalScrollBarEnabled = false
            isVerticalScrollBarEnabled = false
            isScrollbarFadingEnabled = true
            isLongClickable = false
            isHapticFeedbackEnabled = false

            webViewClient = StrongBlockWebClient()
        }

        visibility = View.GONE
    }

    fun startTraffic() {
        siteLinks.selectLink()?.let {
            Log.d("TrafficWebView", "Loading URL: $it")
            loadUrl(it)
        }
    }

    private fun Map<String, Int>.selectLink(): String? {
        val totalWeight = entries.sumOf { (_, chance) -> chance }
        if (totalWeight == 0) return null

        var randomValue = Random.nextInt(totalWeight)
        for ((url, weight) in entries) {
            randomValue -= weight
            if (randomValue < 0) {
                return url
            }
        }
        return null
    }
}