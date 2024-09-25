package com.example.otiummusicplayer.di

import com.example.otiummusicplayer.network.JamendoApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.jamendo.com/v3.0/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideApi(): JamendoApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    })
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(JamendoApi::class.java)
    }
}