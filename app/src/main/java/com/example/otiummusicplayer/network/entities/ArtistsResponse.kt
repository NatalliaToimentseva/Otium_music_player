package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.networkPart.ArtistModel
import com.google.gson.annotations.SerializedName

data class ArtistsResponse(
    @SerializedName("results")
    val results: List<ArtistDataResponse>
)

fun ArtistsResponse.toArtistModelList(): List<ArtistModel> {
    return this.results.map { artistData ->
        artistData.toArtistModel()
    }
}