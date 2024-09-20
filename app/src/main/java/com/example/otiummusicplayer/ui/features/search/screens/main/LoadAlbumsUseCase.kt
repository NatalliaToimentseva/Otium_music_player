package com.example.otiummusicplayer.ui.features.search.screens.main

import com.example.otiummusicplayer.models.AlbumModel
import com.example.otiummusicplayer.network.entities.toAlbumModel
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.ui.features.search.screens.main.domain.NetworkResult
import javax.inject.Inject

class LoadAlbumsUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadAlbums(limit: String, offset: Int): NetworkResult {
        val albums = arrayListOf<AlbumModel>()
        val response = repository.loadAlbums(limit, offset)
        return if (response.isSuccessful) {
            response.body()?.let {
                val albs = it.results
                for (alb in albs) {
                    albums.add(alb.toAlbumModel())
                }
            }
            NetworkResult.Success(albums)
        } else {
            NetworkResult.Error(Throwable(response.code().toString()))
        }
    }
}