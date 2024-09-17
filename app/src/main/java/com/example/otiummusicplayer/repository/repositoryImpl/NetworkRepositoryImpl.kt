package com.example.otiummusicplayer.repository.repositoryImpl

import com.example.otiummusicplayer.network.JamendoApi
import com.example.otiummusicplayer.network.entities.Album
import com.example.otiummusicplayer.network.entities.Artists
import com.example.otiummusicplayer.network.entities.SearchResult
import com.example.otiummusicplayer.network.entities.Track
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import retrofit2.Response
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val api: JamendoApi
) : PlayerNetworkRepository {

    override suspend fun loadAlbums(): Response<Album> = api.getAlbums()

    override suspend fun loadArtists(): Response<Artists> = api.getArtists()

    override suspend fun loadTracksByAlbumId(id: String): Response<Track> = api.getTracksByAlbum()

    override suspend fun loadAlbumsByArtistId(id: String): Response<Album> = api.getAlbumsByArtist()

    override suspend fun searchAllByQuery(searchData: String): Response<SearchResult> = api.searchByQuery()
}