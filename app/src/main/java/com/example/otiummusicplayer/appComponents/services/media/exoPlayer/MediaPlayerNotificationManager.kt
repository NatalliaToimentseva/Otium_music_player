package com.example.otiummusicplayer.appComponents.services.media.exoPlayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat.Token
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.appComponents.services.media.constants.K
import com.example.otiummusicplayer.utils.loadImageWithGlide
import com.example.otiummusicplayer.utils.loadPicture
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class MediaPlayerNotificationManager(
    context: Context,
    sessionToken: Token,
    notificationListener: NotificationListener
) {

    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        val builder = PlayerNotificationManager.Builder(
            context,
            K.NOTIFICATION_ID,
            K.NOTIFICATION_CHANNEL_ID
        )
        with(builder) {
            setMediaDescriptionAdapter(DescriptionAdapter(context, mediaController))
            setNotificationListener(notificationListener)
            setChannelNameResourceId(R.string.notification_channel)
            setChannelDescriptionResourceId(R.string.notification_channel_description)
            setNextActionIconResourceId(R.drawable.btn_next)
            setPreviousActionIconResourceId(R.drawable.btn_prev)
            setStopActionIconResourceId(R.drawable.btn_close)
        }
        notificationManager = builder.build()
        with(notificationManager) {
            setMediaSessionToken(sessionToken)
            setSmallIcon(R.drawable.ic_play)
            setUseNextAction(true)
            setUsePreviousAction(true)
            setUseRewindAction(false)
            setUseFastForwardAction(false)
            setUseStopAction(true)
        }
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    inner class DescriptionAdapter(
        private val context: Context,
        private val controller: MediaControllerCompat
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
            return controller.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? =
            controller.sessionActivity

        override fun getCurrentContentText(player: Player): CharSequence? = null

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val newUri = controller.metadata.description.mediaUri?.path
            val newUrl =
                controller.metadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI)

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
                    val newImage = loadPicture(controller.metadata.description.mediaUri?.path)
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