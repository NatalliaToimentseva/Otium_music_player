package com.example.otiummusicplayer.ui.features.search.screens.tracks

import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.search.screens.tracks.domain.TrackListResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadTracksByAlbumIdUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadTracks(id: String): Flow<TrackListResult> {
        return flow {
            emit(TrackListResult.Loading)
            val response = repository.loadTracksByAlbumId(id)
            if (response.isSuccessful) {
                emit(TrackListResult.Success(response.body()?.results?.firstOrNull()))
            } else {
                emit(TrackListResult.Error(Throwable(response.message())))
            }
        }.catch { e ->
            emit(TrackListResult.Error(Throwable(e.message)))
        }
    }
}