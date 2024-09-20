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

    override suspend fun loadAlbums(limit: String, offset: Int): Response<Album> =
        api.getAlbums(limit = limit, offset = offset)

    override suspend fun loadArtists(limit: String, offset: Int): Response<Artists> =
        api.getArtists(limit = limit, offset = offset)

    override suspend fun loadTracksByAlbumId(id: Int): Response<Track> =
        api.getTracksByAlbum(id = id)

    override suspend fun loadTrack(id: Int): Response<Track> = api.getTrack(id = id)

    override suspend fun loadAlbumsByArtistId(id: String): Response<Album> = api.getAlbumsByArtist(artistID = id)

    override suspend fun searchAllByQuery(searchData: String): Response<SearchResult> =
        api.searchByQuery(query = searchData)
}