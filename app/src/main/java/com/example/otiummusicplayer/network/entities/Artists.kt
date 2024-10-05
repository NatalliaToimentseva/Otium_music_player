package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.networkPart.ArtistModel
import com.google.gson.annotations.SerializedName

data class Artists(
    @SerializedName("results")
    val results: List<ArtistData>
)

fun Artists.toArtistModelList(): List<ArtistModel> {
    return this.results.map { artistData ->
        artistData.toArtistModel()
    }
}