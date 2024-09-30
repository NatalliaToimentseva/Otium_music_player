package com.example.otiummusicplayer.ui.features.playerControlScreen

import android.database.sqlite.SQLiteConstraintException
import com.example.otiummusicplayer.models.networkPart.TrackModel
import com.example.otiummusicplayer.models.networkPart.toTrackDbEntity
import com.example.otiummusicplayer.repository.TracksDbRepository
import javax.inject.Inject

class AddToFavoriteUseCase @Inject constructor(
    private val repository: TracksDbRepository
) {

    suspend fun addTrackToFavorite(trackModel: TrackModel) {
        try {
            repository.addToFavorite(trackModel.toTrackDbEntity())
        } catch (e: SQLiteConstraintException) {
            e.stackTrace
        }
    }
}