package com.example.otiummusicplayer.ui.features.workWithNetworkPart.tracksScreen

import com.example.otiummusicplayer.network.entities.toTrackModelList
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.workWithNetworkPart.tracksScreen.domain.TrackListResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadTracksByAlbumIdUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadTracks(id: Int): Flow<TrackListResult> {
        return flow {
            emit(TrackListResult.Loading)
            val response = repository.loadTracksByAlbumId(id)
            if (response.isSuccessful) {
                val tracks = response.body()?.toTrackModelList() ?: arrayListOf()
                emit(TrackListResult.Success(tracks))
            } else {
                emit(TrackListResult.Error(Throwable(response.code().toString())))
            }
        }.catch { e ->
            emit(TrackListResult.Error(Throwable(e.message)))
        }
    }
}