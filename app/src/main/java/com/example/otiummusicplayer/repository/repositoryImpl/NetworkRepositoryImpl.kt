package com.example.otiummusicplayer.repository.repositoryImpl

import com.example.otiummusicplayer.network.JamendoApi
import com.example.otiummusicplayer.network.entities.AlbumResponse
import com.example.otiummusicplayer.network.entities.ArtistsResponse
import com.example.otiummusicplayer.network.entities.TrackResponse
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import retrofit2.Response
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val api: JamendoApi
) : PlayerNetworkRepository {

    override suspend fun loadAlbums(limit: String, offset: Int): Response<AlbumResponse> =
        api.getAlbums(limit = limit, offset = offset)

    override suspend fun loadArtists(limit: String, offset: Int): Response<ArtistsResponse> =
        api.getArtists(limit = limit, offset = offset)

    override suspend fun loadTracksByAlbumId(id: Int): Response<TrackResponse> =
        api.getTracksByAlbum(id = id)

    override suspend fun loadAlbumsByArtistId(id: String): Response<AlbumResponse> = api.getAlbumsByArtist(artistID = id)

    override suspend fun searchTracksByQuery(searchData: String, limit: String, offset: Int): Response<TrackResponse> =
        api.searchByQuery(query = searchData, limit = limit, offset = offset)
}