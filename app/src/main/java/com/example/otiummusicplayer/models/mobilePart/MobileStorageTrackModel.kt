package com.example.otiummusicplayer.models.mobilePart

import android.net.Uri

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
