package com.example.otiummusicplayer.repository

import androidx.paging.PagingSource
import com.example.otiummusicplayer.roomDB.entity.TracksDbEntity

interface TracksDbRepository {

    suspend fun addToFavorite(track: TracksDbEntity)

    suspend fun deleteFromFavorite(id: String)

    fun loadFavoriteListPage(): PagingSource<Int, TracksDbEntity>

    suspend fun checkIfInFavorite(id: String): TracksDbEntity?
}