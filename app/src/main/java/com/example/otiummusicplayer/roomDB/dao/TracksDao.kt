package com.example.otiummusicplayer.roomDB.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.otiummusicplayer.roomDB.entity.TracksDbEntity

@Dao
interface TracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrack(track: TracksDbEntity)

    @Query("DELETE FROM Tracks WHERE id = :id")
    suspend fun deleteTrack(id: String)

    @Query("SELECT * FROM Tracks")
    fun getTracksPage(): PagingSource<Int, TracksDbEntity>

    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrackById(id: String): TracksDbEntity?
}