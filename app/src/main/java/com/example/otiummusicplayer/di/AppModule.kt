package com.example.otiummusicplayer.di

import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import com.example.otiummusicplayer.repository.PlaylistDbRepository
import com.example.otiummusicplayer.repository.TracksDbRepository
import com.example.otiummusicplayer.repository.repositoryImpl.NetworkRepositoryImpl
import com.example.otiummusicplayer.repository.repositoryImpl.PlaylistDbRepositoryImpl
import com.example.otiummusicplayer.repository.repositoryImpl.TracksDbRepositoryImpl
import com.example.otiummusicplayer.appComponents.services.servicesImpl.FileDownloaderImpl
import com.example.otiummusicplayer.appComponents.services.FileDownloader
import com.example.otiummusicplayer.appComponents.contentProviders.MobileStorageMusicProvider
import com.example.otiummusicplayer.appComponents.contentProviders.contentProvidersImpl.MobileStorageMusicProviderImpl
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
    abstract fun bindPlaylistDbRepository(
        playlistDbRepository: PlaylistDbRepositoryImpl
    ): PlaylistDbRepository

    @Binds
    @Singleton
    abstract fun bindFileDownloader(
        fileDownloader: FileDownloaderImpl
    ): FileDownloader

    @Binds
    @Singleton
    abstract fun bindMobileStorageMusicProvider(
        mobileStorageMusicProvider: MobileStorageMusicProviderImpl
    ): MobileStorageMusicProvider
}


