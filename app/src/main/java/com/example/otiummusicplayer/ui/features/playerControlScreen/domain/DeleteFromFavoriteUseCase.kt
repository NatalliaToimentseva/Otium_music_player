package com.example.otiummusicplayer.ui.features.playerControlScreen.domain

import android.database.sqlite.SQLiteConstraintException
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.repository.TracksDbRepository
import javax.inject.Inject

class DeleteFromFavoriteUseCase @Inject constructor(
    private val repository: TracksDbRepository
) {

    suspend fun deleteTrackFromFavorite(trackModel: TrackModel) {
        try {
            repository.deleteFromFavorite(trackModel.id)
        } catch (e: SQLiteConstraintException) {
            e.stackTrace
        }
    }
}