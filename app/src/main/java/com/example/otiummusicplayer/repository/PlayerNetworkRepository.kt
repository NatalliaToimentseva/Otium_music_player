package com.example.otiummusicplayer.repository

import com.example.otiummusicplayer.network.entities.Album
import com.example.otiummusicplayer.network.entities.Artists
import com.example.otiummusicplayer.network.entities.Track
import retrofit2.Response

interface PlayerNetworkRepository {

    suspend fun loadAlbums(limit: String, offset: Int): Response<Album>

    suspend fun loadArtists(limit: String, offset: Int): Response<Artists>

    suspend fun loadTracksByAlbumId(id: Int): Response<Track>

    suspend fun loadAlbumsByArtistId(id: String): Response<Album>

    suspend fun searchTracksByQuery(searchData: String, limit: String, offset: Int): Response<Track>
}