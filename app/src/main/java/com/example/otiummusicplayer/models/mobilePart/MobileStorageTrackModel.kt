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
    val folderId: Int,
    val path: String
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
        path = path,
        audio = Uri.parse(path).toString(),
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