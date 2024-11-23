package com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain

import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.network.entities.toTrackModelList
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.google.gson.JsonParseException
import java.io.IOException
import javax.inject.Inject

class SearchTracksUseCase @Inject constructor(
    private val repository: PlayerNetworkRepository
) {

    suspend fun searchTrackByQuery(query: String, limit: String, offset: Int): PagingResult<TrackModel> {
        return  try {
            val response = repository.searchTracksByQuery(query, limit, offset)
            if (response.isSuccessful) {
                val tracks = response.body()?.toTrackModelList() ?: arrayListOf()
                PagingResult.Success(tracks)
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