package com.example.otiummusicplayer.network

import com.example.otiummusicplayer.network.entities.Album
import com.example.otiummusicplayer.network.entities.Artists
import com.example.otiummusicplayer.network.entities.SearchResult
import com.example.otiummusicplayer.network.entities.Track
import retrofit2.http.GET
import retrofit2.http.Query

private const val CLIENT_ID = "92a6a782"
private const val FORMAT = "json"

interface Api {

    @GET("albums/")
    suspend fun getAlbums(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT
    ): Album

    @GET("artists/")
    suspend fun getArtists(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("hasimage") hasImage: Boolean = true
    ): Artists

    @GET("albums/tracks/")
    suspend fun getTracksByAlbum(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("id") id: String = "170153"
    ): Track

    @GET("albums/")
    suspend fun getAlbumsByArtist(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("artist_id") artistID: String = "502048"
    ): Album

    @GET("autocomplete/")
    suspend fun searchByQuery(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("prefix") query: String = "vigor"
    ): SearchResult
}