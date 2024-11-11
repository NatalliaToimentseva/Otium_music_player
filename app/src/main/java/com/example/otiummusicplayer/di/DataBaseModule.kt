package com.example.otiummusicplayer.di

import android.content.Context
import androidx.room.Room
import com.example.otiummusicplayer.roomDB.AppDataBase
import com.example.otiummusicplayer.roomDB.dao.PlaylistsDao
import com.example.otiummusicplayer.roomDB.dao.TracksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATA_BASE_NAME = "app_data_base"

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java, DATA_BASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideTracksDao(appDataBase: AppDataBase): TracksDao {
        return appDataBase.getTracksDao()
    }

    @Provides
    @Singleton
    fun providePlaylistDao(appDataBase: AppDataBase): PlaylistsDao {
        return appDataBase.getPlaylistDao()
    }
}