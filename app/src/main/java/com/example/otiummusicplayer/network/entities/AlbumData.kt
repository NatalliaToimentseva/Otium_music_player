package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.networkPart.AlbumModel
import com.google.gson.annotations.SerializedName

data class AlbumData(
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("artist_id")
    val artistId: String,
    @SerializedName("artist_name")
    val artistName: String,
    @SerializedName("releasedate")
    val releaseDate: String,
    @SerializedName("shareurl")
    val shareUrl: String,
    @SerializedName("zip")
    val zip: String,
    @SerializedName("zip_allowed")
    val zipAllowed: Boolean
)

fun AlbumData.toAlbumModel() = AlbumModel(
    id, image, name, artistId, artistName, shareUrl, zip, zipAllowed
)