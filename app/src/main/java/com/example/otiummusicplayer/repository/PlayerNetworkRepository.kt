package com.example.otiummusicplayer.repository

import com.example.otiummusicplayer.network.entities.AlbumResponse
import com.example.otiummusicplayer.network.entities.ArtistsResponse
import com.example.otiummusicplayer.network.entities.TrackResponse
import retrofit2.Response

interface PlayerNetworkRepository {

    suspend fun loadAlbums(limit: String, offset: Int): Response<AlbumResponse>

    suspend fun loadArtists(limit: String, offset: Int): Response<ArtistsResponse>

    suspend fun loadTracksByAlbumId(id: Int): Response<TrackResponse>

    suspend fun loadAlbumsByArtistId(id: String): Response<AlbumResponse>

    suspend fun searchTracksByQuery(searchData: String, limit: String, offset: Int): Response<TrackResponse>
}