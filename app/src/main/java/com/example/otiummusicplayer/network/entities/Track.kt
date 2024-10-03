package com.example.otiummusicplayer.network.entities
import com.example.otiummusicplayer.models.networkPart.TrackModel

data class Track(
    val results: List<TrackData>
)

fun Track.toTrackModelList(): List<TrackModel> {
    return this.results.map { trackData ->
        trackData.toTrackModel()
    }
}