package ru.bgitu.components.sitetraffic

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

class StrongBlockWebClient : WebViewClient() {

    override fun shouldInterceptRequest(
        webView: WebView,
        request: WebResourceRequest
    ): WebResourceResponse? {
        if (shouldBlockUrl(request.url.toString())) return null
        return super.shouldInterceptRequest(webView, request)
    }



    private fun shouldBlockUrl(url: String): Boolean {
        val blockedExt = arrayOf(
            ".css",
            ".ico",
            ".woff"
        ).any { it in url }

        return blockedExt
    }
}