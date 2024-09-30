package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.networkPart.TrackModel

data class TrackData(
    val id: String,
    val name: String,
    val image: String,
    val audio: String,
    val duration: Int,
    val album_name: String,
    val album_id: String,
    val artist_name: String,
    val audiodownload: String,
    val audiodownload_allowed: Boolean,
    val shareurl: String
)

fun TrackData.toTrackModel(): TrackModel = TrackModel(
    id = id,
    name = name,
    image = image,
    audio = audio,
    duration = duration,
    albumName = album_name,
    albumId = album_id,
    artistName = artist_name,
    audioDownload = audiodownload,
    isDownloadAllowed = audiodownload_allowed,
    shareUrl = shareurl,
    isFavorite = false
)