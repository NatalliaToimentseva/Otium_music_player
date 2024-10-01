package com.example.otiummusicplayer.roomDB.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.otiummusicplayer.roomDB.entity.PlaylistsEntity

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createPlaylist(list: PlaylistsEntity)

    @Query("DELETE FROM Playlists WHERE id IN (:id)")
    suspend fun deletePlaylist(id: List<Long>)

    @Query("SELECT * FROM Playlists")
    fun getPlaylists(): PagingSource<Int, PlaylistsEntity>
}