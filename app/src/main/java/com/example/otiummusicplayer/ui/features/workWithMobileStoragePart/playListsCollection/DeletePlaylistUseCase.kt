package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection

import android.database.sqlite.SQLiteConstraintException
import com.example.otiummusicplayer.repository.PlaylistDbRepository
import javax.inject.Inject

class DeletePlaylistUseCase @Inject constructor(
    private val repository: PlaylistDbRepository
) {

    suspend fun deletePlayList(id: Long) {
        try {
            repository.deletePlaylistById(id)
        } catch (e: SQLiteConstraintException) {
            e.stackTrace
        }
    }
}