package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.networkPart.AlbumModel

data class Album(
    val results: List<AlbumData>
)

fun Album.toAlbumModelList(): List<AlbumModel> {
    return this.results.map { albumData ->
        albumData.toAlbumModel()
    }
}