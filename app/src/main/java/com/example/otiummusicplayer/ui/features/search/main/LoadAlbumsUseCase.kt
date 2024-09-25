package com.example.otiummusicplayer.ui.features.search.main

import com.example.otiummusicplayer.models.AlbumModel
import com.example.otiummusicplayer.network.entities.toAlbumModelList
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.search.main.domain.PagingNetworkResult
import javax.inject.Inject

class LoadAlbumsUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadAlbums(limit: String, offset: Int): PagingNetworkResult<AlbumModel> {
        val response = repository.loadAlbums(limit, offset)
        return if (response.isSuccessful) {
            val albums = response.body()?.toAlbumModelList() ?: arrayListOf()
            PagingNetworkResult.Success(albums)
        } else {
            PagingNetworkResult.Error(Throwable(response.message()))
        }
    }
}