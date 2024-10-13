package com.example.otiummusicplayer.media.exoPlayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat.Token
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.media.constants.K
import com.example.otiummusicplayer.utils.loadPicture
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener

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
            setMediaDescriptionAdapter(DescriptionAdapter((mediaController)))
            setNotificationListener((notificationListener))
            setChannelNameResourceId(R.string.notification_channel)
            setChannelDescriptionResourceId(R.string.notification_channel_description)
        }
        notificationManager = builder.build()
        with(notificationManager) {
            setMediaSessionToken(sessionToken)
            setSmallIcon(R.drawable.ic_play)
            setUseNextAction(true)
            setUsePreviousAction(true)
            setUseRewindAction(false)
            setUseFastForwardAction(false)
        }
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    inner class DescriptionAdapter(private val controller: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {

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
            return loadPicture(controller.metadata.description.mediaUri?.path)
        }
    }
}