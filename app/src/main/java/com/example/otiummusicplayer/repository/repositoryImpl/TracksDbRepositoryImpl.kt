package com.example.otiummusicplayer.repository.repositoryImpl

import androidx.paging.PagingSource
import com.example.otiummusicplayer.repository.TracksDbRepository
import com.example.otiummusicplayer.roomDB.dao.TracksDao
import com.example.otiummusicplayer.roomDB.entity.TracksDbEntity
import javax.inject.Inject

class TracksDbRepositoryImpl @Inject constructor(
    private val dao: TracksDao
) : TracksDbRepository {

    override suspend fun addToFavorite(track: TracksDbEntity) = dao.saveTrack(track)

    override suspend fun deleteFromFavorite(id: String) = dao.deleteTrack(id)

    override fun loadFavoriteListPage(): PagingSource<Int, TracksDbEntity> = dao.getTracksPage()

    override suspend fun checkIfInFavorite(id: String): TracksDbEntity? = dao.getTrackById(id)
}