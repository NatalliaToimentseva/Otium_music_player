package com.example.otiummusicplayer.models

import android.os.Parcelable
import com.example.otiummusicplayer.roomDB.entity.TracksDbEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackModel(
    val id: String,
    val name: String,
    val image: String,
    val audio: String,
    val duration: Int,
    val albumName: String,
    val albumId: String,
    val artistName: String,
    val audioDownload: String,
    val isDownloadAllowed: Boolean,
    val shareUrl: String,
    val isFavorite: Boolean
) : Parcelable

fun TrackModel.toTrackDbEntity(): TracksDbEntity {
    return TracksDbEntity(
        0,
        id,
        name,
        image,
        audio,
        duration,
        albumName,
        albumId,
        albumName,
        audioDownload,
        isDownloadAllowed,
        shareUrl,
        true
    )
}