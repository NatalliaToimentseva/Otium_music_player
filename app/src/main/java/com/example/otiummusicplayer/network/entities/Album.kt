package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.networkPart.AlbumModel
import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("results")
    val results: List<AlbumData>
)

fun Album.toAlbumModelList(): List<AlbumModel> {
    return this.results.map { albumData ->
        albumData.toAlbumModel()
    }
}