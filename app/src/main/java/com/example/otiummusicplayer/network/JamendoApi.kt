package com.example.otiummusicplayer.network

import com.example.otiummusicplayer.BuildConfig
import com.example.otiummusicplayer.network.entities.AlbumResponse
import com.example.otiummusicplayer.network.entities.ArtistsResponse
import com.example.otiummusicplayer.network.entities.TrackResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//API URL - https://developer.jamendo.com/v3.0/docs
//TEST song url = "https://prod-1.storage.jamendo.com/?trackid=1467858&format=mp31&from=QeBfVL5FlSP5h7JRU1OYQQ%3D%3D%7C5OZLVhPfv%2Ff9Z7jFE1nvDw%3D%3D"

private const val STANDART_LIMIT = "100"
private const val FORMAT = "json"

interface JamendoApi {

    @GET("albums/")
    suspend fun getAlbums(
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("limit") limit: String,
        @Query("offset") offset: Int
    ): Response<AlbumResponse>

    @GET("albums/")
    suspend fun getAlbumsByArtist(
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("limit") limit: String = STANDART_LIMIT,
        @Query("artist_id") artistID: String
    ): Response<AlbumResponse>

    @GET("artists/")
    suspend fun getArtists(
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("hasimage") hasImage: Boolean = true,
        @Query("limit") limit: String,
        @Query("offset") offset: Int
    ): Response<ArtistsResponse>

    @GET("tracks/")
    suspend fun getTracksByAlbum(
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("limit") limit: String = STANDART_LIMIT,
        @Query("album_id") id: Int,
    ): Response<TrackResponse>

    @GET("tracks/")
    suspend fun searchByQuery(
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("format") format: String = FORMAT,
        @Query("search") query: String,
        @Query("limit") limit: String,
        @Query("offset") offset: Int
    ): Response<TrackResponse>
}