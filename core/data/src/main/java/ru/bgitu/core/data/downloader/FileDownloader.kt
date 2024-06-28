package ru.bgitu.core.data.downloader

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.File
import java.util.concurrent.TimeUnit

interface FileDownloaderService {
    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody
}

class FileDownloader(
    private val ioDispatcher: CoroutineDispatcher
) {
    private val downloadService: FileDownloaderService =
        Retrofit.Builder()
            .baseUrl("http://notused/")
            .client(
                OkHttpClient.Builder()
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build()
            )
            .build()
            .create()

    suspend fun downloadAndSaveFile(url: String, destination: File): Flow<DownloadState> {
        return flow {
            val response = try {
                downloadService.downloadFile(url)
            } finally {

            }
            val contentLegth = response.contentLength()

            response.byteStream().use { input ->
                destination.outputStream().use { output ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var progressBytes = 0L
                    var bytes = input.read(buffer)

                    while (bytes >= 0) {
                        output.write(buffer, 0, bytes)
                        progressBytes += bytes
                        bytes = input.read(buffer)
                        emit(
                            DownloadState.Downloading(
                                totalBytesToDownload = contentLegth,
                                bytesDownloaded = progressBytes
                            )
                        )
                    }
                }
            }
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
}