package com.example.otiummusicplayer.repository

import com.example.otiummusicplayer.network.entities.Album
import com.example.otiummusicplayer.network.entities.Artists
import com.example.otiummusicplayer.network.entities.SearchResult
import com.example.otiummusicplayer.network.entities.Track
import retrofit2.Response

interface PlayerNetworkRepository {

    suspend fun loadAlbums(): Response<Album>

    suspend fun loadArtists(): Response<Artists>

    suspend fun loadTracksByAlbumId(id: String): Response<Track>

    suspend fun loadAlbumsByArtistId(id: String): Response<Album>

    suspend fun searchAllByQuery(searchData: String): Response<SearchResult>
}