package com.example.otiummusicplayer.ui.features.search.screens.main

import com.example.otiummusicplayer.network.entities.Album
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkSearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadAlbumByArtistUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadAlbumByArtist(id: String): Flow<NetworkSearchResult> {
        return flow {
            emit(NetworkSearchResult.Loading)
            val response = repository.loadAlbumsByArtistId(id)
            if (response.isSuccessful) {
                emit(NetworkSearchResult.SuccessAlbumByArtist(response.body() ?: Album(results = arrayListOf())))
            } else {
                emit(NetworkSearchResult.Error(Throwable(response.message())))
            }
        }.catch { e ->
            emit(NetworkSearchResult.Error(Throwable(e.message)))
        }
    }
}