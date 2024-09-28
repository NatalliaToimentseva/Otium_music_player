package com.example.otiummusicplayer.roomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.otiummusicplayer.roomDB.dao.TracksDao
import com.example.otiummusicplayer.roomDB.entity.TracksDbEntity

@Database(
    version = 1,
    entities = [
        TracksDbEntity::class
    ],
)
abstract class AppDataBase: RoomDatabase() {

    abstract fun getTracksDao(): TracksDao
}