package com.example.otiummusicplayer.ui.features.networkPart.mainScreen

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.repository.TracksDbRepository
import com.example.otiummusicplayer.roomDB.entity.TracksDbEntity
import com.example.otiummusicplayer.roomDB.entity.toTrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val INIT_PAGE_SIZE = 10
private const val INIT_LOAD_SIZE = 20

class LoadFavoriteDataUseCase @Inject constructor(
    private val repository: TracksDbRepository
) {

    fun loadFavoriteListPage(): Flow<PagingData<TrackModel>> = Pager(
        PagingConfig(
            pageSize = INIT_PAGE_SIZE,
            prefetchDistance = INIT_LOAD_SIZE
        )
    ) {
        repository.loadFavoriteListPage()
    }.flow
        .map { value: PagingData<TracksDbEntity> ->
            value.map { tracksDbEntity: TracksDbEntity ->
                tracksDbEntity.toTrackModel()
            }
        }
}