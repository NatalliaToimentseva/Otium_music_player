package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.ArtistModel

data class Artists(
    val results: List<ArtistData>
)

fun Artists.toArtistModelList(): List<ArtistModel> {
    val artists = arrayListOf<ArtistModel>()
    for (artist in this.results) {
        artists.add(artist.toArtistModel())
    }
    return artists
}