package com.example.otiummusicplayer.ui.features.workWithMobileStoragePart.playListsCollection

import android.database.sqlite.SQLiteConstraintException
import com.example.otiummusicplayer.models.mobilePart.PlayerPlayListModel
import com.example.otiummusicplayer.models.mobilePart.toPlaylistsEntity
import com.example.otiummusicplayer.repository.PlaylistDbRepository
import javax.inject.Inject

class AddPlayListUseCase @Inject constructor(
    private val repository: PlaylistDbRepository
) {

    suspend fun addPlayList(playListModel: PlayerPlayListModel) {
        try {
            repository.addPlaylist(playListModel.toPlaylistsEntity())
        } catch (e: SQLiteConstraintException) {
            e.stackTrace
        }
    }
}