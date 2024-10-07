package com.example.otiummusicplayer.repository

import androidx.paging.PagingSource
import com.example.otiummusicplayer.roomDB.entity.TracksDbEntity

interface TracksDbRepository {

    fun loadFavoriteListPage(playlistId: Long): PagingSource<Int, TracksDbEntity>

    suspend fun checkIfInFavorite(id: String): TracksDbEntity?

    suspend fun addToFavorite(track: TracksDbEntity)

    suspend fun deleteFromFavorite(id: String)

    fun loadPlaylistTracks(playlistId: Long): PagingSource<Int, TracksDbEntity>

    suspend fun addToPlaylist(tracks: List<TracksDbEntity>)

    suspend fun deleteFromPlaylist(tracksId: List<String>)
}