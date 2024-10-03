package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.networkPart.ArtistModel

data class Artists(
    val results: List<ArtistData>
)

fun Artists.toArtistModelList(): List<ArtistModel> {
    return this.results.map { artistData ->
        artistData.toArtistModel()
    }
}