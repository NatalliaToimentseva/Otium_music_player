package com.example.otiummusicplayer.ui.features.search.main

import com.example.otiummusicplayer.models.ArtistModel
import com.example.otiummusicplayer.network.entities.toArtistModelList
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.search.main.domain.PagingNetworkResult
import javax.inject.Inject

class LoadArtistsUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadArtist(limit: String, offset: Int): PagingNetworkResult<ArtistModel> {
        val response = repository.loadArtists(limit, offset)
        return if (response.isSuccessful) {
            val artists = response.body()?.toArtistModelList() ?: arrayListOf()
            PagingNetworkResult.Success(artists)
        } else {
            PagingNetworkResult.Error(Throwable(response.message()))
        }
    }
}