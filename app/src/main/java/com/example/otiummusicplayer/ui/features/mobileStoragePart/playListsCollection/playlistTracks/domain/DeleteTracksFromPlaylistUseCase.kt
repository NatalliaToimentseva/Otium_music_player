package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.playlistTracks.domain

import android.database.sqlite.SQLiteConstraintException
import com.example.otiummusicplayer.repository.TracksDbRepository
import javax.inject.Inject

class DeleteTracksFromPlaylistUseCase @Inject constructor(
    private val repository: TracksDbRepository
) {

    suspend fun deleteTracks(tracksId: List<String>) {
        try {
            repository.deleteFromPlaylist(tracksId)
        } catch (e: SQLiteConstraintException) {
            e.stackTrace
        }
    }
}