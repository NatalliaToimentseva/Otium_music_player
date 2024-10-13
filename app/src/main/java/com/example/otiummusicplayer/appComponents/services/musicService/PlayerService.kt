package com.example.otiummusicplayer.appComponents.services.musicService

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import com.example.otiummusicplayer.R
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.utils.loadPicture
import com.example.otiummusicplayer.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

const val NOTIFICATION_ID = 1
const val ACTION_PREVIOUS = "action previous"
const val ACTION_NEXT = "action next"
const val ACTION_PLAY_PAUSE = "action play/pause"
const val ACTION_STOP = "action stop"
private const val MEDIA_SESSION_TAG = "MediaPlayerService"

@AndroidEntryPoint
class PlayerService : Service() {

    @Inject
    lateinit var myPlayerNotificationManager: PlayerNotificationManager
    private var mediaSession: MediaSessionCompat? = null
    private val myBinder = PlayerBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var tracks: List<TrackModel> = arrayListOf()
    private var currentPosition: TrackModel? = null
    private var isPlaying: Boolean = false

    inner class PlayerBinder : Binder() {

        val service: PlayerService
            get() = this@PlayerService
    }

    override fun onCreate() {
        super.onCreate()
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
        } catch (e: IOException) {
            e.stackTrace
        }
        mediaSession = MediaSessionCompat(this, MEDIA_SESSION_TAG)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
//        myBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()
        startPlayer()
        mediaPlayer?.setOnCompletionListener {
            playNext()
        }

        when (intent?.action) {
            ACTION_PLAY_PAUSE -> {
                this.toast("Play was clicked")
                playStop()
                updateNotification()
            }

            ACTION_NEXT -> {
                this.toast("Pause was clicked")
            }

            ACTION_PREVIOUS -> {
                this.toast("Pause was clicked")
            }

            ACTION_STOP -> {
                this.toast("Stop was clicked")
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaSession?.release()
    }

    fun loadData(allTracks: List<TrackModel>, position: TrackModel) {
        tracks = allTracks
        currentPosition = position
        preparePlayer(position)
    }

    private fun startForegroundService() {
        mediaSession?.let { session ->
            val notification = myPlayerNotificationManager.buildNotification(
                session,
                if (isPlaying) {
                    R.drawable.btn_pause
                } else {
                    R.drawable.btn_play
                },
                getCurrentImage()
            )
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun getCurrentImage(): Bitmap {
        val currentImage = currentPosition?.run {
            if (image != null) {
                getBitmapFromUrl(image)
            } else {
                loadPicture(path)
            }
        }
        val pic = BitmapFactory.decodeResource(this.resources, R.drawable.bg_sound)
        Log.d("AAA", "pic =$pic")
        return pic
    }

    private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun updateNotification() {
        mediaSession?.let { session ->
            val notification = myPlayerNotificationManager.buildNotification(
                session,
                if (isPlaying) {
                    R.drawable.btn_pause
                } else {
                    R.drawable.btn_play
                },
                getCurrentImage()
            )
            val playerNotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            playerNotificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    fun playStop() {
        if (isPlaying) {
            isPlaying = false
            pausePlayer()
        } else {
            isPlaying = true
            startPlayer()
        }
    }

    fun createPlayer() {
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
        } catch (e: IOException) {
            e.stackTrace
        }
    }

    fun preparePlayer(position: TrackModel?) {
        try {
            mediaPlayer?.setDataSource(position?.audio)
            mediaPlayer?.prepare()
        } catch (e: IOException) {
            e.stackTrace
        }
    }

    fun startPlayer() {
        mediaPlayer?.start()
        isPlaying = true
    }

    fun pausePlayer() {
        mediaPlayer?.pause()
        isPlaying = false
    }

    fun playNext(): TrackModel? {
        currentPosition?.let { position ->
            if(tracks.isNotEmpty()) {
                val index = tracks.indexOfFirst { it.id == position.id }
                if (index != -1 && (index + 1) <= (tracks.size - 1)) {
                    currentPosition = tracks[index + 1]
                    mediaPlayer?.reset()
                    preparePlayer(currentPosition)
                    if (isPlaying) startPlayer()
                }
            }
        }
        return currentPosition
    }

    fun playPrevious(): TrackModel? {
        currentPosition?.let { position ->
            if(tracks.isNotEmpty()) {
                val index = tracks.indexOfFirst { it.id == position.id }
                if (index != -1 && (index - 1) >= 0) {
                    currentPosition = tracks[index - 1]
                    mediaPlayer?.reset()
                    preparePlayer(currentPosition)
                    if (isPlaying) startPlayer()
                }
            }
        }
        return currentPosition
    }

    fun resetPlayer() {
        mediaPlayer?.reset()
    }

    fun releasePlayer() {
        mediaPlayer?.release()
    }

    fun setDataToPlayer() {
//        mediaPlayer?.setDataSource(track.audio)
    }

    fun getDuration() {
        mediaPlayer?.duration
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    fun isLooping(): Boolean {
        return mediaPlayer?.isLooping == true
    }

    fun onCompletionListener(listener: () -> Unit) {
        mediaPlayer?.setOnCompletionListener {
            listener.invoke()
        }
    }
}