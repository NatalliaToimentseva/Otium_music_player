package com.example.otiummusicplayer.models

import android.os.Parcelable
import com.example.otiummusicplayer.roomDB.entity.TracksDbEntity
import com.example.otiummusicplayer.utils.formatTimeToMls
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackModel(
    val id: String,
    val name: String,
    val image: String?,
    val audio: String,
    val duration: String,
    val albumName: String,
    val albumId: String?,
    val artistName: String,
    val audioDownload: String?,
    val isDownloadAllowed: Boolean?,
    val shareUrl: String?,
    val isFavorite: Boolean?,
    val playlistId: Long?
) : Parcelable

fun TrackModel.toTrackDbEntity(playlistId: Long?): TracksDbEntity {
    return TracksDbEntity(
        dbId = 0,
        id = id,
        trackName = name,
        trackImage = image,
        trackAudio = audio,
        duration = formatTimeToMls(duration),
        albumName = albumName,
        albumId = albumId,
        artistName = artistName,
        audioDownload = audioDownload,
        isDownloadAllowed = isDownloadAllowed,
        shareUrl = shareUrl,
        isFavorite = true,
        playlistId = playlistId
    )
}

fun List<TrackModel>.toListTrackDbEntity(playlistId: Long?): List<TracksDbEntity> {
    return this.map { item -> item.toTrackDbEntity(playlistId) }
}