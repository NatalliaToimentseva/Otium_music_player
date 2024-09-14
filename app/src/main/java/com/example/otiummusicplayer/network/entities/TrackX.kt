package com.example.otiummusicplayer.network.entities

data class TrackX(
    val audio: String,
    val audiodownload: String,
    val audiodownload_allowed: Boolean,
    val duration: String,
    val id: String,
    val license_ccurl: String,
    val name: String,
    val position: String
)