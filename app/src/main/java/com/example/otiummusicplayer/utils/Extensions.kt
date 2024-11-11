package com.example.otiummusicplayer.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.paging.ItemSnapshotList
import com.example.otiummusicplayer.models.TrackModel
import com.google.gson.GsonBuilder

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ItemSnapshotList<TrackModel>.toStringGsonFormat(): String {
    return GsonBuilder().create().toJson(this as List<TrackModel>)
}

@OptIn(UnstableApi::class)
fun List<TrackModel>.toListMediaItem(): List<MediaItem> = this.map { audio ->
    buildMediaItem(audio)
}

@OptIn(UnstableApi::class)
private fun buildMediaItem(audio: TrackModel): MediaItem {
    return MediaItem.Builder()
        .setMediaId(audio.id)
        .setUri(audio.audio)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(audio.name)
                .setArtist(audio.artistName)
                .setAlbumTitle(audio.albumName)
//                    .setArtworkData(getImage(audio, context))
                .setDurationMs(formatTimeToMls(audio.duration).toLong())
                .build()
        )
        .build()
}

//fun Context.getImage(audio: TrackModel): ByteArray {
//    var image: Bitmap? = null
//    if (audio.image != null) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val downloadedImage = loadImageWithGlide(audio.image, this@getImage)
//            withContext(Dispatchers.Main) {
//                image = downloadedImage
//            }
//        }
//    } else if (audio.path != null) {
//        image = loadPicture(audio.path)
//    }
//    val byteArrayOutputStream = ByteArrayOutputStream()
//    if (image != null) {
//        image?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//    } else {
//        BitmapFactory.decodeResource(this.resources, R.drawable.bg_sound)
//            .compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//    }
//    return byteArrayOutputStream.toByteArray()
//}
