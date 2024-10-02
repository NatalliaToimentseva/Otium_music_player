package com.example.otiummusicplayer.ui.features.workWithNetworkPart.mainScreen

import com.example.otiummusicplayer.models.networkPart.AlbumModel
import com.example.otiummusicplayer.network.entities.toAlbumModelList
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.workWithNetworkPart.mainScreen.domain.PagingResult
import javax.inject.Inject

class LoadAlbumsUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadAlbums(limit: String, offset: Int): PagingResult<AlbumModel> {
        val response = repository.loadAlbums(limit, offset)
        return if (response.isSuccessful) {
            val albums = response.body()?.toAlbumModelList() ?: arrayListOf()
            PagingResult.Success(albums)
        } else {
            PagingResult.Error(Throwable(response.message()))
        }
    }
}