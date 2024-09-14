package com.example.otiummusicplayer.network.entities

data class TrackData(
    val artist_id: String,
    val artist_name: String,
    val id: String,
    val image: String,
    val name: String,
    val releasedate: String,
    val tracks: List<TrackX>,
    val zip: String,
    val zip_allowed: Boolean
)
