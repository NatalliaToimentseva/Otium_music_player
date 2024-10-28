package com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain

import com.example.otiummusicplayer.network.entities.toAlbumModelList
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadAlbumByArtistUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadAlbumsByArtist(id: String): Flow<NetworkResult> {
        return flow {
            emit(NetworkResult.Loading)
            val response = repository.loadAlbumsByArtistId(id)
            if (response.isSuccessful) {
                val albums = response.body()?.toAlbumModelList() ?: arrayListOf()
                emit(NetworkResult.SuccessAlbumByArtist(albums))
            } else {
                emit(NetworkResult.Error(Throwable(response.code().toString())))
            }
        }.catch { e ->
            emit(NetworkResult.Error(Throwable(e.message)))
        }
    }
}