package com.example.otiummusicplayer.models.networkPart

data class AlbumModel(
    val id: String,
    val image: String,
    val name: String,
    val artistId: String,
    val artistName: String,
    val shareUrl: String,
    val zip: String,
    val isZipAllowed: Boolean
)


