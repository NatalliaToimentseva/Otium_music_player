package com.example.otiummusicplayer.ui.features.playerControlScreen

import android.database.sqlite.SQLiteConstraintException
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.repository.TracksDbRepository
import com.example.otiummusicplayer.roomDB.entity.toTrackModel
import javax.inject.Inject

class CheckInFavoriteUseCase @Inject constructor(
    private val repository: TracksDbRepository
) {

    suspend fun checkIsTrackInFavorite(id: String): TrackModel? {
        return try {
            repository.checkIfInFavorite(id)?.toTrackModel()
        } catch (e: SQLiteConstraintException) {
            e.stackTrace
            null
        }
    }
}