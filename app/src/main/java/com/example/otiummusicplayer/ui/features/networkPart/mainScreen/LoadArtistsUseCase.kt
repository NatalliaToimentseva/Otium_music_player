package com.example.otiummusicplayer.ui.features.networkPart.mainScreen

import com.example.otiummusicplayer.models.networkPart.ArtistModel
import com.example.otiummusicplayer.network.entities.toArtistModelList
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain.PagingResult
import javax.inject.Inject

class LoadArtistsUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadArtist(limit: String, offset: Int): PagingResult<ArtistModel> {
        val response = repository.loadArtists(limit, offset)
        return if (response.isSuccessful) {
            val artists = response.body()?.toArtistModelList() ?: arrayListOf()
            PagingResult.Success(artists)
        } else {
            PagingResult.Error(Throwable(response.message()))
        }
    }
}