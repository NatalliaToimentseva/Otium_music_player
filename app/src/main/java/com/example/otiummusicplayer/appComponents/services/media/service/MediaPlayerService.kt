package com.example.otiummusicplayer.appComponents.services.media.service

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

//    private val serviceJob = SupervisorJob()
//    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
//    private var mediaPlayerNotificationManager: MediaPlayerNotificationManager? = null

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var controllerFuture: ListenableFuture<MediaController>

    @Inject
    lateinit var mediaSession: MediaSession
//    private var mediaController: MediaController? = null
//
//    override fun onCreate() {
//        super.onCreate()
//        mediaSession.let { session ->
//            controllerFuture.addListener({
//                mediaController = controllerFuture.get()
//            }, MoreExecutors.directExecutor())
//            mediaController?.let { controller ->
////                mediaPlayerNotificationManager = MediaPlayerNotificationManager(
////                    context = this,
////                    mediaController = controller,
////                    sessionToken = session.platformToken,
////                    notificationListener = PlayerNotificationListener()
////                )
////                mediaPlayerNotificationManager?.showNotification(exoPlayer)
//            }
//        }
//    }

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