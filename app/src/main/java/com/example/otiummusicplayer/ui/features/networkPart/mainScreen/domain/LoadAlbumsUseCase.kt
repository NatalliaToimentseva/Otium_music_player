package com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain

import com.example.otiummusicplayer.models.networkPart.AlbumModel
import com.example.otiummusicplayer.network.entities.toAlbumModelList
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.google.gson.JsonParseException
import java.io.IOException
import javax.inject.Inject

class LoadAlbumsUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadAlbums(limit: String, offset: Int): PagingResult<AlbumModel> {
        return try {
            val response = repository.loadAlbums(limit, offset)
            if (response.isSuccessful) {
                val albums = response.body()?.toAlbumModelList() ?: arrayListOf()
                PagingResult.Success(albums)
            } else {
                PagingResult.Error(Throwable(response.message()))
            }
        } catch (e: IOException) {
            PagingResult.Error(Throwable(e.message))
        } catch (e: JsonParseException) {
            PagingResult.Error(Throwable(e.message))
        }
    }
}