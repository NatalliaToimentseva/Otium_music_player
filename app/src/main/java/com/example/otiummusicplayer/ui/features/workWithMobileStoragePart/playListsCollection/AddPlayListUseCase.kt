package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection

import android.database.sqlite.SQLiteConstraintException
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import com.example.otiummusicplayer.models.mobilePart.toPlaylistsEntity
import com.example.otiummusicplayer.repository.PlaylistDbRepository
import com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection.domain.PlaylistErrors
import javax.inject.Inject

class AddPlayListUseCase @Inject constructor(
    private val repository: PlaylistDbRepository
) {

    suspend fun addPlayList(playListModel: PlayerPlayListModel): PlaylistErrors? {
        return try {
            repository.addPlaylist(playListModel.toPlaylistsEntity())
            null
        } catch (e: SQLiteConstraintException) {
            PlaylistErrors.Error(e.message ?: "Error, please try again")
        }
    }
}