package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.domain

import android.database.sqlite.SQLiteConstraintException
import com.example.otiummusicplayer.repository.TracksDbRepository
import javax.inject.Inject

class DeleteTracksWithPlaylistUseCase @Inject constructor(
    private val repository: TracksDbRepository
) {

    suspend fun deleteWithPlaylist(playlistId: List<Long>) {
        try {
            repository.deleteTracksWithPlaylist(playlistId)
        } catch (e: SQLiteConstraintException) {
            e.stackTrace
        }
    }
}