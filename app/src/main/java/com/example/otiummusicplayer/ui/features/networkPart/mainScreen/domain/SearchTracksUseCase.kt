package com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain

import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.network.entities.toTrackModelList
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import javax.inject.Inject

class SearchTracksUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun searchTrackByQuery(query: String, limit: String, offset: Int): PagingResult<TrackModel> {
        val response = repository.searchTracksByQuery(query, limit, offset)
        return if (response.isSuccessful) {
            val tracks = response.body()?.toTrackModelList() ?: arrayListOf()
            PagingResult.Success(tracks)
        } else {
            PagingResult.Error(Throwable(response.message()))
        }
    }
}