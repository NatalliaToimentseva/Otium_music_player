package com.example.otiummusicplayer.ui.features.search.screens.main

import com.example.otiummusicplayer.models.ArtistModel
import com.example.otiummusicplayer.network.entities.toArtistModel
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkResult
import javax.inject.Inject

class LoadArtistsUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadArtist(limit: String, offset: Int): NetworkResult {
        val artists = arrayListOf<ArtistModel>()
        val response = repository.loadArtists(limit, offset)
        return if (response.isSuccessful) {
            response.body()?.let {
                val arts = it.results
                for (art in arts) {
                    artists.add(art.toArtistModel())
                }
            }
            NetworkResult.Success(artists)
        } else {
            NetworkResult.Error(Throwable(response.code().toString()))
        }
    }
}
