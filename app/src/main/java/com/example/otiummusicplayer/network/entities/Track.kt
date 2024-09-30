package com.example.otiummusicplayer.network.entities
import com.example.otiummusicplayer.models.networkPart.TrackModel

data class Track(
    val results: List<TrackData>
)

fun Track.toTrackModelList(): List<TrackModel> {
    val tracks = arrayListOf<TrackModel>()
    for (track in this.results) {
        tracks.add(track.toTrackModel())
    }
    return tracks
}