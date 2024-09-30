package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.networkPart.ArtistModel

data class ArtistData(
    val id: String,
    val image: String,
    val joindate: String,
    val name: String,
    val shareurl: String,
    val shorturl: String,
    val website: String
)

fun ArtistData.toArtistModel() = ArtistModel(
    id, image, name, shareurl
)