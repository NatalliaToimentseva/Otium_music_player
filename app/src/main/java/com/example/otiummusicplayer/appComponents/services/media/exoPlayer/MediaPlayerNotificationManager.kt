package com.example.otiummusicplayer.appComponents.services.media.exoPlayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.OptIn
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.appComponents.services.media.constants.K
import com.example.otiummusicplayer.utils.loadImageWithGlide
import com.example.otiummusicplayer.utils.loadPicture
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.ui.PlayerNotificationManager
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@UnstableApi
class MediaPlayerNotificationManager @OptIn(UnstableApi::class) constructor
    (
    context: Context,
    mediaController: MediaController,
//    controllerFuture: ListenableFuture<MediaController>,
    sessionToken: android.media.session.MediaSession.Token,
    notificationListener: PlayerNotificationManager.NotificationListener
) {

//    private var mediaController: MediaController? = null

    private val notificationManager: PlayerNotificationManager = PlayerNotificationManager.Builder(
        context,
        K.NOTIFICATION_ID,
        K.NOTIFICATION_CHANNEL_ID
    )
        .setMediaDescriptionAdapter(DescriptionAdapter(context, mediaController))
        .setNotificationListener(notificationListener)
        .setChannelNameResourceId(R.string.notification_channel)
        .setChannelDescriptionResourceId(R.string.notification_channel_description)
        .setNextActionIconResourceId(R.drawable.btn_next)
        .setPreviousActionIconResourceId(R.drawable.btn_prev)
        .setStopActionIconResourceId(R.drawable.btn_close)
        .build().apply {
            setMediaSessionToken(sessionToken)
            setSmallIcon(R.drawable.ic_play)
            setUseNextAction(true)
            setUsePreviousAction(true)
            setUseRewindAction(false)
            setUseFastForwardAction(false)
            setUseStopAction(true)
        }

//    init {
//        controllerFuture.addListener({
//            mediaController = controllerFuture.get()
//        }, MoreExecutors.directExecutor())
//    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotification(player: Player) {
        Log.d("AAA", "MediaPlayerNotificationManager showNotification was called")
        notificationManager.setPlayer(player)
    }

    inner class DescriptionAdapter(
        private val context: Context,
        private val controller: MediaController?
    ) :
        PlayerNotificationManager.MediaDescriptionAdapter {

        private var cachedImage: Bitmap? =
            BitmapFactory.decodeResource(context.resources, R.drawable.bg_sound)
            set(value) = if (value != null) {
                field = value
            } else field = BitmapFactory.decodeResource(context.resources, R.drawable.bg_sound)
        private var cachedURL: String? = null
        private var cachedURI: String? = null

        override fun getCurrentContentTitle(player: Player): CharSequence {
            return controller?.mediaMetadata?.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? =
            controller?.sessionActivity

        override fun getCurrentContentText(player: Player): CharSequence {
            return controller?.mediaMetadata?.artist.toString()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val newUri = controller?.mediaMetadata?.artworkUri?.path //description.mediaUri?.path
            val newUrl: String? = null
//                controller.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI)

            return if (
                (cachedURL == null && cachedURI == null) ||
                (newUrl != cachedURL || newUri != cachedURI)
            ) {
                if (newUrl != null) {
                    cachedURL = newUrl
                    CoroutineScope(Dispatchers.IO).launch {
                        val newImage: Bitmap? = loadImageWithGlide(newUrl, context)
                        withContext(Dispatchers.Main) {
                            callback.onBitmap(
                                newImage ?: BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.bg_sound
                                )
                            )
                        }
                    }
                    return null
                } else if (newUri != null && newUri.length > 2) {
                    val newImage =
                        loadPicture(controller?.mediaMetadata?.artworkUri?.path) //controller.metadata.description.mediaUri?.path
                    cachedURI = newUri
                    cachedImage = newImage
                    return cachedImage
                } else return BitmapFactory.decodeResource(context.resources, R.drawable.bg_sound)
            } else {
                cachedImage
            }
        }
    }
}