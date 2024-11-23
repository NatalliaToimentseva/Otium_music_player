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
    return GsonBuilder().create().toJson(this)
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
                .setDurationMs(formatTimeToMls(audio.duration).toLong())
                .build()
        )
        .build()
}
