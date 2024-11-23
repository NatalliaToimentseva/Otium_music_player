package com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain

import com.example.otiummusicplayer.models.networkPart.ArtistModel
import com.example.otiummusicplayer.network.entities.toArtistModelList
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.google.gson.JsonParseException
import java.io.IOException
import javax.inject.Inject

class LoadArtistsUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun loadArtist(limit: String, offset: Int): PagingResult<ArtistModel> {
        return try {
            val response = repository.loadArtists(limit, offset)
            if (response.isSuccessful) {
                val artists = response.body()?.toArtistModelList() ?: arrayListOf()
                PagingResult.Success(artists)
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