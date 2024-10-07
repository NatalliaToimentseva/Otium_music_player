package com.example.otiummusicplayer.models.mobilePart

import android.net.Uri
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.utils.formatTimeMls

data class MobileStorageTrackModel(
    val id: Long,
    val title: String,
    val name: String,
    val album: String,
    val artist: String,
    val duration: Int,
    val uri: Uri,
    val folderId: Int
)

fun MobileStorageTrackModel.toTrackModel(): TrackModel {
    val trTitle = if (this.title[0].isUpperCase()) {
        title
    } else {
        name
    }
    return TrackModel(
        id = id.toString(),
        name = trTitle,
        image = null,
        audio = uri.toString(),
//        uri = uri,
        duration = formatTimeMls(duration),
        albumName = album,
        albumId = null,
        artistName = artist,
        audioDownload = null,
        isDownloadAllowed = null,
        shareUrl = null,
        isFavorite = null,
        playlistId = -1
    )
}

fun List<MobileStorageTrackModel>.toListTrackModel(): List<TrackModel> {
    return this.map { mobileStorageTrackModel ->
        mobileStorageTrackModel.toTrackModel()
    }
}