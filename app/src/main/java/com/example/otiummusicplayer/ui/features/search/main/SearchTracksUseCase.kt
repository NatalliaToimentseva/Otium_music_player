package com.example.otiummusicplayer.ui.features.search.main

import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.network.entities.toTrackModelList
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.search.main.domain.PagingNetworkResult
import javax.inject.Inject

class SearchTracksUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun searchTrackByQuery(query: String, limit: String, offset: Int): PagingNetworkResult<TrackModel> {
        val response = repository.searchTracksByQuery(query, limit, offset)
        return if (response.isSuccessful) {
            val tracks = response.body()?.toTrackModelList() ?: arrayListOf()
            PagingNetworkResult.Success(tracks)
        } else {
            PagingNetworkResult.Error(Throwable(response.message()))
        }
    }
}

//return flow {
//    emit(NetworkResult.Loading)
//    val response = repository.searchTracksByQuery(query)
//    if (response.isSuccessful) {
//        val tracks = response.body()?.toTrackModelList() ?: arrayListOf()
//        emit(NetworkResult.SuccessSearchTracks(tracks))
//    } else {
//        emit(NetworkResult.Error(Throwable(response.code().toString())))
//    }
//}.catch { e ->
//    emit(NetworkResult.Error(Throwable(e.message)))
//}