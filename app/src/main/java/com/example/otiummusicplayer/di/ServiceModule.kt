package com.example.otiummusicplayer.di

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.AudioAttributes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import com.example.otiummusicplayer.appComponents.services.musicService.MediaPlayerService
import com.example.otiummusicplayer.ui.features.MainActivity
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideSessionToken(@ApplicationContext context: Context): SessionToken =
        SessionToken(context, ComponentName(context, MediaPlayerService::class.java))

    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context,

        ): ExoPlayer = ExoPlayer.Builder(context)
        .build()
        .apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                true
            )
            setHandleAudioBecomingNoisy(true)
        }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): MediaSession {
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntent(Intent(context, MainActivity::class.java))
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }
        return MediaSession.Builder(context, player)
            .setSessionActivity(pendingIntent)
            .build()
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideControllerFuture(
        @ApplicationContext context: Context,
        mediaSession: MediaSession
    ): ListenableFuture<MediaController> {
        return MediaController.Builder(
            context,
            mediaSession.token
        ).buildAsync()
    }
}