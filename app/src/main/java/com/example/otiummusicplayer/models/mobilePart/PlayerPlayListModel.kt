package com.example.otiummusicplayer.models.mobilePart

import com.example.otiummusicplayer.roomDB.entity.PlaylistsEntity

data class PlayerPlayListModel(
    val id: Long,
    val playListTitle: String
)

fun PlayerPlayListModel.toPlaylistsEntity(): PlaylistsEntity {
    return PlaylistsEntity(id, playListTitle)
}
