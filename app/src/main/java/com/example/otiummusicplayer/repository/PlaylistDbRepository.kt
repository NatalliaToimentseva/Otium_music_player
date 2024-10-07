package com.example.otiummusicplayer.repository

import androidx.paging.PagingSource
import com.example.otiummusicplayer.roomDB.entity.PlaylistsEntity

interface PlaylistDbRepository {

    fun getPlaylists(): PagingSource<Int, PlaylistsEntity>

    suspend fun addPlaylist(playlistsEntity: PlaylistsEntity)

    suspend fun deletePlaylistById(id: List<Long>)
}