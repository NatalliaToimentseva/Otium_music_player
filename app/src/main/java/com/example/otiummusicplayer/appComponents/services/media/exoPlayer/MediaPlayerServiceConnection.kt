package com.example.otiummusicplayer.appComponents.services.media.exoPlayer

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.example.otiummusicplayer.appComponents.services.media.constants.K
import com.example.otiummusicplayer.appComponents.services.media.service.MediaPlayerService
import com.example.otiummusicplayer.models.TrackModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MediaPlayerServiceConnection @Inject constructor(
    @ApplicationContext context: Context
) {

    var mediaControllerCompat: MediaControllerCompat? = null
    private var audioList = listOf<TrackModel>()
    val currentPlayingAudio: MutableStateFlow<TrackModel?> = MutableStateFlow(null)
    val rootMediaId: String get() = mediaBrowser.root
    private val transportControl: MediaControllerCompat.TransportControls?
        get() = mediaControllerCompat?.transportControls

    private val _playBackState: MutableStateFlow<PlaybackStateCompat?> = MutableStateFlow(null)
    val playBackState: StateFlow<PlaybackStateCompat?> get() = _playBackState

    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> get() = _isConnected

    private val mediaBrowserServiceCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MediaPlayerService::class.java),
        mediaBrowserServiceCallback,
        null
    ).apply {
        connect()
    }

    fun playAudio(tracks: List<TrackModel>) {
        audioList = tracks
        mediaBrowser.sendCustomAction(K.START_MEDIA_PLAY_ACTION, null, null)
    }

    fun playFromMedia(mediaId: String) {
        transportControl?.playFromMediaId(mediaId, null)
    }

    fun playTrack() {
        transportControl?.play()
    }

    fun pauseTrack() {
        transportControl?.pause()
    }

    fun stopTrack() {
        transportControl?.stop()
    }

    fun seekTo(position: Long) {
        transportControl?.seekTo(
            position
        )
    }

    fun skipToNext() {
        transportControl?.skipToNext()
    }

    fun skipToPrevious() {
        transportControl?.skipToPrevious()
    }

    fun onShuffleMode() {
        transportControl?.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
    }

    fun offShuffleMode() {
        transportControl?.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)
    }

    fun onRepeatMode() {
        transportControl?.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE)
    }

    fun offRepeatMode() {
        transportControl?.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE)
    }

    fun subscribe(
        parentId: String,
        callBack: MediaBrowserCompat.SubscriptionCallback
    ) {
        mediaBrowser.subscribe(parentId, callBack)
    }

    fun unSubscribe(
        parentId: String,
        callBack: MediaBrowserCompat.SubscriptionCallback
    ) {
        mediaBrowser.unsubscribe(parentId, callBack)
    }

    fun refreshMediaBrowserChildren() {
        mediaBrowser.sendCustomAction(K.REFRESH_MEDIA_PLAY_ACTION, null, null)
    }

    inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            _isConnected.value = true
            mediaControllerCompat = MediaControllerCompat(
                context,
                mediaBrowser.sessionToken
            ).apply {
                registerCallback(MediaControllerCallback())
            }
        }

        override fun onConnectionSuspended() {
            _isConnected.value = false
        }

        override fun onConnectionFailed() {
            _isConnected.value = false
        }
    }

    inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            _playBackState.value = state
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            currentPlayingAudio.value = metadata?.let { data ->
                audioList.find {
                    it.id == data.description.mediaId
                }
            }
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            mediaBrowserServiceCallback.onConnectionSuspended()
        }
    }
}