package com.example.otiummusicplayer.models

import android.os.Parcelable
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
    val shareUrl: String
): Parcelable