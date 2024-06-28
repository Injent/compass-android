package ru.bgitu.core.data.repository

import android.graphics.Bitmap
import android.util.Base64
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.bgitu.core.datastore.SettingsRepository
import ru.bgitu.core.model.UserRole
import ru.bgitu.core.network.CompassService
import ru.bgitu.core.network.model.request.HeadmanRequest
import java.io.ByteArrayOutputStream

class DefaultHeadmanRepository(
    private val compassService: CompassService,
    private val settings: SettingsRepository,
    private val ioDispatcher: CoroutineDispatcher
) : HeadmanRepository {
    override suspend fun headmanRequest(
        bitmap: Bitmap,
        comment: String
    ) {
        withContext(ioDispatcher) {
            val encodedImage = run {
                val output = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
                Base64.encodeToString(output.toByteArray(), Base64.DEFAULT)
            }
            settings.data.first().compassAccount?.let {
                compassService.headmanRequest(
                    HeadmanRequest(
                        base64photo = encodedImage,
                        role = UserRole.HEADMAN,
                        comment = comment
                    )
                )
            } ?: false
        }
    }
}