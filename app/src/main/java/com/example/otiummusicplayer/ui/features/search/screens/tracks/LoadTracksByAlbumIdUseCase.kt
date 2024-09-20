package com.example.otiummusicplayer.ui.features.search.screens.tracks

import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.network.entities.toTrackModel
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.search.screens.tracks.domain.TrackListResult
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
            val albumTracks = arrayListOf<TrackModel>()
            val response = repository.loadTracksByAlbumId(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    val tracks = it.results
                    for (track in tracks) {
                        albumTracks.add(track.toTrackModel())
                    }
                }
                emit(TrackListResult.Success(albumTracks))
            } else {
                emit(TrackListResult.Error(Throwable(response.code().toString())))
            }
        }.catch { e ->
            emit(TrackListResult.Error(Throwable(e.message)))
        }
    }
}