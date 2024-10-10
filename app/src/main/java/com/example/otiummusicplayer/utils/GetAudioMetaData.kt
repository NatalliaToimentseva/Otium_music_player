package com.example.otiummusicplayer.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import java.io.File

fun loadPicture(absolutePath: String?): Bitmap? {
    if (absolutePath == null || !File(absolutePath).exists()) {
        return null
    }
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(absolutePath)
    val artwork = retriever.embeddedPicture.let { it ->
        it?.let { it1 -> BitmapFactory.decodeByteArray(it, 0, it1.size) }
    }
    retriever.release()
    return artwork
}