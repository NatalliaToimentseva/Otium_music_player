package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks

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

class LoadPlaylistTracksUseCase @Inject constructor(
    private val tracksDbRepository: TracksDbRepository
) {

    fun loadTracks(playlistId: Long): Flow<PagingData<TrackModel>> = Pager(
        PagingConfig(
            pageSize = INIT_PAGE_SIZE,
            prefetchDistance = INIT_LOAD_SIZE
        )
    ) {
        tracksDbRepository.loadPlaylistTracks(playlistId)
    }.flow
        .map { value: PagingData<TracksDbEntity> ->
            value.map { tracksDbEntity: TracksDbEntity ->
                tracksDbEntity.toTrackModel()
            }
        }
}