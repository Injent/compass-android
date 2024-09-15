package ru.bgitu.core.data.downloader

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Uses Android native tools to do download files from https as Ktor CIO client can't do it
 * See [YouTrack Issue](https://youtrack.jetbrains.com/issue/KTOR-5392/CIO-The-Received-alert-during-handshake-error-with-TLS-v1.3-server)
 */
class AndroidFileDownloader(
    private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun downloadAndSaveFile(url: String, destination: File): Flow<DownloadState> = flow {
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
            ).also { emit(it) }
        }

        output.flush()
        output.close()
        input.close()

        emit(DownloadState.Finished)
    }
        .onStart {
            emit(DownloadState.Downloading(0, 0))
        }
        .catch {
            it.printStackTrace()
            emit(DownloadState.Failed(it))
        }
        .flowOn(ioDispatcher)
}