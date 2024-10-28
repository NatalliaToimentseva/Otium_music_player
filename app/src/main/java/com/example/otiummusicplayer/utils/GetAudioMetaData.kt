package com.example.otiummusicplayer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

fun loadPicture(absolutePath: String?): Bitmap? {
    if (absolutePath == null || !File(absolutePath).exists()) {
        return null
    }
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(absolutePath)
    val artwork = retriever.embeddedPicture?.let {
        BitmapFactory.decodeByteArray(it, 0, it.size)
    }
    retriever.release()
    return artwork
}

suspend fun loadImageWithGlide(url: String, context: Context): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        } catch (e: Exception) {
            null
        }
    }
}