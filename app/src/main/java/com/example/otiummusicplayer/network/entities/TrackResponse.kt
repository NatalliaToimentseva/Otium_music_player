package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.TrackModel
import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("results")
    val results: List<TrackDataResponse>
)

fun TrackResponse.toTrackModelList(): List<TrackModel> {
    return this.results.map { trackData ->
        trackData.toTrackModel()
    }
}