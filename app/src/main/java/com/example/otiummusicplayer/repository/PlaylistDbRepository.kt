package com.example.otiummusicplayer.repository

import androidx.paging.PagingSource
import com.example.otiummusicplayer.roomDB.entity.PlaylistsEntity

interface PlaylistDbRepository {

    suspend fun addPlaylist(playlistsEntity: PlaylistsEntity)

    suspend fun deletePlaylistById(id: Long)

    fun getPlaylists(): PagingSource<Int, PlaylistsEntity>
}