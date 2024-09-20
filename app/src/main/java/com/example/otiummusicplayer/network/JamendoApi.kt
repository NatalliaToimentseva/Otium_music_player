package com.example.otiummusicplayer.network

import com.example.otiummusicplayer.network.entities.Album
import com.example.otiummusicplayer.network.entities.Artists
import com.example.otiummusicplayer.network.entities.SearchResult
import com.example.otiummusicplayer.network.entities.Track
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//API URL - https://developer.jamendo.com/v3.0/docs
//TEST song url = "https://prod-1.storage.jamendo.com/?trackid=1467858&format=mp31&from=QeBfVL5FlSP5h7JRU1OYQQ%3D%3D%7C5OZLVhPfv%2Ff9Z7jFE1nvDw%3D%3D"

private const val CLIENT_ID = "92a6a782"
private const val FORMAT = "json"
private const val STANDART_LIMIT = "100"
//private const val TEST_ALBUM_ID = "170153"
//private const val TEST_ARTIST_ID = "502048"
//private const val TEST_QUERY = "vigor"

interface JamendoApi {

    @GET("albums/")
    suspend fun getAlbums(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("limit") limit: String,
        @Query("offset") offset: Int
    ): Response<Album>

    @GET("artists/")
    suspend fun getArtists(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("hasimage") hasImage: Boolean = true,
        @Query("limit") limit: String,
        @Query("offset") offset: Int
    ): Response<Artists>

    @GET("tracks/")
    suspend fun getTracksByAlbum(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("limit") limit: String = STANDART_LIMIT,
        @Query("album_id") id: Int,
    ): Response<Track>

    @GET("tracks/")
    suspend fun getTrack(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("id") id: Int,
    ): Response<Track>

    @GET("albums/")
    suspend fun getAlbumsByArtist(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("limit") limit: String = STANDART_LIMIT,
        @Query("artist_id") artistID: String
    ): Response<Album>

    @GET("autocomplete/")
    suspend fun searchByQuery(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("prefix") query: String
    ): Response<SearchResult>
}