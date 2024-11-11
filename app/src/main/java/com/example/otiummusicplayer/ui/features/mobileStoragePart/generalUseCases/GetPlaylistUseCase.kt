package com.example.otiummusicplayer.ui.features.mobileStoragePart.generalUseCases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import com.example.otiummusicplayer.repository.PlaylistDbRepository
import com.example.otiummusicplayer.roomDB.entity.PlaylistsEntity
import com.example.otiummusicplayer.roomDB.entity.toPlayerPlayLists
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val INIT_PAGE_SIZE = 10
private const val INIT_LOAD_SIZE = 20

class GetPlaylistUseCase @Inject constructor(
    private val repository: PlaylistDbRepository
) {

    fun getPlaylistsPage(): Flow<PagingData<PlayerPlayListModel>> = Pager(
        PagingConfig(
            pageSize = INIT_PAGE_SIZE,
            prefetchDistance = INIT_LOAD_SIZE
        )
    ) {
        repository.getPlaylists()
    }.flow
        .map { value: PagingData<PlaylistsEntity> ->
            value.map { playlistsEntity: PlaylistsEntity ->
                playlistsEntity.toPlayerPlayLists()
            }
        }
}