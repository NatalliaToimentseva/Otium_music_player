package com.example.otiummusicplayer.ui.features.mobileStoragePart.generalUseCases

import android.database.sqlite.SQLiteConstraintException
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.models.toListTrackDbEntity
import com.example.otiummusicplayer.repository.TracksDbRepository
import javax.inject.Inject

class AddTracksToPlaylistUseCase @Inject constructor(
    private val tracksDbRepository: TracksDbRepository
) {

    suspend fun addTracksToPlaylist(tracks: List<TrackModel>, playlistId: Long) {
        try {
            tracksDbRepository.addToPlaylist(tracks = tracks.toListTrackDbEntity(playlistId))
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
        }
    }
}