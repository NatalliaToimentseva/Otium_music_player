package com.example.otiummusicplayer.appComponents.services.servicesImpl

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.ui.features.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val CHANNEL_ID = "media_playback_channel"
private const val NOTIFICATION_CHANNEL_NAME = "notification channel 1"

class PlayerNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

//    val customLayout = RemoteViews(context.packageName, R.layout.custom_notification)
//    .setCustomContentView(customLayout)

    fun buildNotification(mediaSession: MediaSessionCompat, iconId: Int, image: Bitmap): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val playPauseIntent = Intent(context, PlayerService::class.java).apply {
            action = ACTION_PLAY_PAUSE
        }
        val playPausePendingIntent =
            PendingIntent.getService(context, 0, playPauseIntent, PendingIntent.FLAG_IMMUTABLE)

        val nextTrackIntent = Intent(context, PlayerService::class.java).apply {
            action = ACTION_NEXT
        }
        val nextTrackPendingIntent =
            PendingIntent.getService(context, 0, nextTrackIntent, PendingIntent.FLAG_IMMUTABLE)

        val previousTrackIntent = Intent(context, PlayerService::class.java).apply {
            action = ACTION_PREVIOUS
        }
        val previousTrackPendingIntent =
            PendingIntent.getService(context, 0, previousTrackIntent, PendingIntent.FLAG_IMMUTABLE)

        val stopIntent = Intent(context, PlayerService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent =
            PendingIntent.getService(context, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_play)
            .setLargeIcon(image)
            .setContentTitle("Media Playback")
            .setContentText("Playing your media")
            .addAction(R.drawable.btn_close, "Stop", stopPendingIntent)
            .addAction(R.drawable.btn_prev, "Previous", previousTrackPendingIntent)
            .addAction(iconId, "Play/Pause", playPausePendingIntent)
            .addAction(R.drawable.btn_next, "Next", nextTrackPendingIntent)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(1, 2, 3)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH
            )
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}