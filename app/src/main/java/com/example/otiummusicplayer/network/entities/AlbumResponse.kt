package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.networkPart.AlbumModel
import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("results")
    val results: List<AlbumDataResponse>
)

fun AlbumResponse.toAlbumModelList(): List<AlbumModel> {
    return this.results.map { albumData ->
        albumData.toAlbumModel()
    }
}