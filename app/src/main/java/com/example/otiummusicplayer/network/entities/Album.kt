package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.AlbumModel

data class Album(
    val results: List<AlbumData>
)

fun Album.toAlbumModelList(): List<AlbumModel> {
    val albums = arrayListOf<AlbumModel>()
    for (album in this.results) {
        albums.add(album.toAlbumModel())
    }
    return albums
}