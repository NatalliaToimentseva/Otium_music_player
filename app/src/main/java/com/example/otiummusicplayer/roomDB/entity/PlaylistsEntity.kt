package com.example.otiummusicplayer.roomDB.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel

@Entity(
    tableName = "Playlists"
)
data class PlaylistsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("title")
    val title: String
)

fun PlaylistsEntity.toPlayerPlayLists(): PlayerPlayListModel {
    return PlayerPlayListModel(id, title)
}