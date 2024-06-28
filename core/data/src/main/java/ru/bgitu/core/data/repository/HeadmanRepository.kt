package ru.bgitu.core.data.repository

import android.graphics.Bitmap

interface HeadmanRepository {
    /**
     * @return true if request was successful otherwise false
     */
    suspend fun headmanRequest(bitmap: Bitmap, comment: String = "")
}