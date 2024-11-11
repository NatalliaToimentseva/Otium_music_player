package com.example.otiummusicplayer.ui.features.playerControlScreen.domain

import android.database.sqlite.SQLiteConstraintException
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.models.toTrackDbEntity
import com.example.otiummusicplayer.repository.TracksDbRepository
import javax.inject.Inject

class AddToFavoriteUseCase @Inject constructor(
    private val repository: TracksDbRepository
) {

    suspend fun addTrackToFavorite(trackModel: TrackModel) {
        try {
            repository.addToFavorite(trackModel.toTrackDbEntity(-1))
        } catch (e: SQLiteConstraintException) {
            e.stackTrace
        }
    }
}