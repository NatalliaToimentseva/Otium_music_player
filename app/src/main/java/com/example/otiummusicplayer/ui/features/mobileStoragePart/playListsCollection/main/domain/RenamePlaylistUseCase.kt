package com.example.otiummusicplayer.ui.features.mobileStoragePart.playListsCollection.main.domain

import android.database.sqlite.SQLiteConstraintException
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import com.example.otiummusicplayer.models.mobilePart.toPlaylistsEntity
import com.example.otiummusicplayer.repository.PlaylistDbRepository
import javax.inject.Inject

class RenamePlaylistUseCase @Inject constructor(
    private val repository: PlaylistDbRepository
) {

    suspend fun renamePlayList(playlist: PlayerPlayListModel): PlaylistErrors? {
        return try {
            repository.updatePlaylist(playlist.toPlaylistsEntity())
            null
        } catch (e: SQLiteConstraintException) {
            PlaylistErrors.Error(e.message ?: "Error, please try again")
        }
    }
}