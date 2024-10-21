package com.example.otiummusicplayer.appComponents.services.media.service

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.example.otiummusicplayer.appComponents.services.media.exoPlayer.MediaPlayerNotificationManager
import com.example.otiummusicplayer.appComponents.services.media.exoPlayer.MediaSource
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaController
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.example.otiummusicplayer.appComponents.services.media.constants.K
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MediaPlayerService : MediaSessionService() {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var mediaPlayerNotificationManager: MediaPlayerNotificationManager? = null
    var isForegroundService: Boolean = false

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var controllerFuture: ListenableFuture<MediaController>

    @Inject
    lateinit var mediaSession: MediaSession
    private var mediaController: MediaController? = null

    override fun onCreate() {
        super.onCreate()
        mediaSession.let { session ->
            controllerFuture.addListener({
                mediaController = controllerFuture.get()
            }, MoreExecutors.directExecutor())
            mediaController?.let { controller ->
//                mediaPlayerNotificationManager = MediaPlayerNotificationManager(
//                    context = this,
//                    mediaController = controller,
//                    sessionToken = session.platformToken,
//                    notificationListener = PlayerNotificationListener()
//                )
//                mediaPlayerNotificationManager?.showNotification(exoPlayer)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        MediaController.releaseFuture(controllerFuture)
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
        exoPlayer.release()
        mediaSession.release()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

//    override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
//        super.onUpdateNotification(session, startInForegroundRequired)
//        session.sendCustomCommand()
//    }

//        private fun preparePlayer() {
//        Log.d("AAA", "Service preparePlayer()")
//            exoPlayer.addMediaItems(listAudio)
//            exoPlayer.seekToDefaultPosition(currentAudio?.mediaId?.toInt()?: 0)
//            exoPlayer.addListener(PlayerEventListener())
//            exoPlayer.prepare()
//            exoPlayer.playWhenReady
//    }

//    inner class NotificationProvider : MediaNotification.Provider {
//
//        override fun createNotification(
//            mediaSession: MediaSession,
//            customLayout: ImmutableList<CommandButton>,
//            actionFactory: MediaNotification.ActionFactory,
//            onNotificationChangedCallback: MediaNotification.Provider.Callback
//        ): MediaNotification {
//            return MediaNotification(
//                K.NOTIFICATION_ID,
//                Notification.Builder(this@MediaPlayerService, K.NOTIFICATION_CHANNEL_ID)
//                    .build()
//            )
//        }
//
//        override fun handleCustomCommand(
//            session: MediaSession,
//            action: String,
//            extras: Bundle
//        ): Boolean {
//            return false
//        }
//    }

//    inner class PlayerNotificationListener : PlayerNotificationManager.NotificationListener {
//
//        init {
//            Log.d("AAA", "Service PlayerNotificationListener was created")
//        }
//
//        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
//            stopForeground(STOP_FOREGROUND_REMOVE)
//            isForegroundService = false
//            stopSelf()
//        }
//
//        override fun onNotificationPosted(
//            notificationId: Int,
//            notification: Notification,
//            ongoing: Boolean
//        ) {
////            notificationId1 = notificationId
////            notification1 = notification
//            Log.d("AAA", "Service onNotificationPosted was called")
//            if (ongoing && !isForegroundService) {
//                Log.d("AAA", "Service onNotificationPosted notification =$notification")
//                ContextCompat.startForegroundService(
//                    applicationContext,
//                    Intent(
//                        applicationContext,
//                        this@MediaPlayerService.javaClass
//                    )
//                )
//                startForeground(notificationId, notification)
//                isForegroundService = true
//            }
//        }
//    }
//
//    private inner class PlayerEventListener : Player.Listener {
//
//        override fun onPlaybackStateChanged(playbackState: Int) {
//            when (playbackState) {
//                Player.STATE_BUFFERING,
//                Player.STATE_READY -> {
//                    mediaPlayerNotificationManager?.showNotification(exoPlayer)
//                }
//
//                else -> {
//                    mediaPlayerNotificationManager?.hideNotification()
//                }
//            }
//        }
//    }
}