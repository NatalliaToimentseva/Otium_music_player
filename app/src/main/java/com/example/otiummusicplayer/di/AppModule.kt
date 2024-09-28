package com.example.otiummusicplayer.di

import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.repository.TracksDbRepository
import com.example.otiummusicplayer.repository.repositoryImpl.NetworkRepositoryImpl
import com.example.otiummusicplayer.repository.repositoryImpl.TracksDbRepositoryImpl
import com.example.otiummusicplayer.utils.FileDownloaderImpl
import com.example.otiummusicplayer.utils.FileDownloader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindPlayerNetworkRepository(
        networkRepository: NetworkRepositoryImpl
    ): PlayerNetworkRepository

    @Binds
    @Singleton
    abstract fun bindTracksDbRepository(
        tracksDbRepositoryImpl: TracksDbRepositoryImpl
    ): TracksDbRepository

    @Binds
    @Singleton
    abstract fun bindFileDownloader(
        fileDownloader: FileDownloaderImpl
    ): FileDownloader
}


