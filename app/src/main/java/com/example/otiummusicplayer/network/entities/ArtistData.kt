package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.networkPart.ArtistModel
import com.google.gson.annotations.SerializedName

data class ArtistData(
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("shareurl")
    val shareUrl: String,
    @SerializedName("website")
    val website: String
)

fun ArtistData.toArtistModel() = ArtistModel(
    id, image, name, shareUrl
)