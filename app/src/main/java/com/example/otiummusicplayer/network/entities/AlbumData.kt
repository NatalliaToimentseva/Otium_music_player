package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.networkPart.AlbumModel

data class AlbumData(
    val id: String,
    val image: String,
    val name: String,
    val artist_id: String,
    val artist_name: String,
    val releasedate: String,
    val shareurl: String,
    val shorturl: String,
    val zip: String,
    val zip_allowed: Boolean
)

fun AlbumData.toAlbumModel() = AlbumModel(
    id, image, name, artist_id, artist_name, shareurl, zip, zip_allowed
)