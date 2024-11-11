package com.example.otiummusicplayer.appComponents.services.musicService

import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MediaPlayerService : MediaSessionService() {

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var controllerFuture: ListenableFuture<MediaController>

    @Inject
    lateinit var mediaSession: MediaSession

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
        exoPlayer.release()
        mediaSession.release()
        MediaController.releaseFuture(controllerFuture)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }
}