package ru.bgitu.core.data.downloader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import ru.bgitu.core.network.crypto.TrustAllX509TrustManager
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext

class AndroidFileDownloader {
    fun downloadAndSaveFile(url: String, destination: File) = callbackFlow<DownloadState> {
        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(TrustAllX509TrustManager()), SecureRandom())
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
        val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        connection.connect()

        val contentLength = connection.contentLength.toLong()
        val input: InputStream = connection.inputStream
        val output = destination.outputStream()

        val data = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytesDownloaded: Long = 0
        var count: Int

        while (input.read(data).also { count = it } != -1) {
            bytesDownloaded += count
            output.write(data, 0, count)

            DownloadState.Downloading(
                totalBytesToDownload = contentLength,
                bytesDownloaded = bytesDownloaded
            ).also { send(it) }
        }

        output.flush()
        output.close()
        input.close()

        send(DownloadState.Finished)
        close()
    }
        .onStart {
            emit(DownloadState.Downloading(0, 0))
        }
        .catch {
            it.printStackTrace()
            emit(DownloadState.Failed(it))
        }
        .flowOn(Dispatchers.IO)
}