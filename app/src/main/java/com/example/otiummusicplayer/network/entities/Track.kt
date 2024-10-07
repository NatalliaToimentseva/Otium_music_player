package com.example.otiummusicplayer.network.entities

import com.example.otiummusicplayer.models.TrackModel
import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("results")
    val results: List<TrackData>
)

fun Track.toTrackModelList(): List<TrackModel> {
    return this.results.map { trackData ->
        trackData.toTrackModel()
    }
}