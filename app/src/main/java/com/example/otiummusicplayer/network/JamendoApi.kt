package com.example.otiummusicplayer.network

import com.example.otiummusicplayer.network.entities.Album
import com.example.otiummusicplayer.network.entities.Artists
import com.example.otiummusicplayer.network.entities.SearchResult
import com.example.otiummusicplayer.network.entities.Track
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//API URL - https://developer.jamendo.com/v3.0/docs

private const val CLIENT_ID = "92a6a782"
private const val FORMAT = "json"

interface JamendoApi {

    @GET("albums/")
    suspend fun getAlbums(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT
    ): Response<Album>

    @GET("artists/")
    suspend fun getArtists(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("hasimage") hasImage: Boolean = true
    ): Response<Artists>

    @GET("albums/tracks/")
    suspend fun getTracksByAlbum(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("id") id: String = "170153"
    ): Response<Track>

    @GET("albums/")
    suspend fun getAlbumsByArtist(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("artist_id") artistID: String = "502048"
    ): Response<Album>

    @GET("autocomplete/")
    suspend fun searchByQuery(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("prefix") query: String = "vigor"
    ): Response<SearchResult>
}