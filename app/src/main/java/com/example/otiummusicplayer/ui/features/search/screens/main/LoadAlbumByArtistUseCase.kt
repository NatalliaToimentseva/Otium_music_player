package com.example.otiummusicplayer.ui.features.search.screens.main

import com.example.otiummusicplayer.models.AlbumModel
import com.example.otiummusicplayer.network.entities.toAlbumModel
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadAlbumByArtistUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadAlbumsByArtist(id: String): Flow<NetworkResult> {
        return flow {
            val albums = arrayListOf<AlbumModel>()
            val response = repository.loadAlbumsByArtistId(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    val albs = it.results
                    for (alb in albs) {
                        albums.add(alb.toAlbumModel())
                    }
                }
                emit(NetworkResult.Success(albums))
            } else {
                emit(NetworkResult.Error(Throwable(response.code().toString())))
            }
        }.catch { e ->
            emit(NetworkResult.Error(Throwable(e.message)))
        }
    }
}