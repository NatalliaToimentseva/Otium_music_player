package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.utils.formatTimeSec
import com.google.gson.annotations.SerializedName

data class TrackDataResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val title: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("audio")
    val audioStream: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("album_name")
    val albumTitle: String,
    @SerializedName("album_id")
    val albumId: String,
    @SerializedName("artist_name")
    val artistName: String,
    @SerializedName("audiodownload")
    val urlToDownload: String,
    @SerializedName("audiodownload_allowed")
    val isDownloadAllowed: Boolean,
    @SerializedName("shareurl")
    val urlToShare: String
)

fun TrackDataResponse.toTrackModel(): TrackModel = TrackModel(
    id = id,
    name = title,
    image = image,
    path = null,
    audio = audioStream,
    duration = formatTimeSec(duration),
    albumName = albumTitle,
    albumId = albumId,
    artistName = artistName,
    audioDownload = urlToDownload,
    isDownloadAllowed = isDownloadAllowed,
    shareUrl = urlToShare,
    isFavorite = false,
    playlistId = -1
)