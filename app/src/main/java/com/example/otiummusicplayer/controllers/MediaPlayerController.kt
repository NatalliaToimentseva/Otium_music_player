package com.example.otiummusicplayer.controllers

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.utils.toListMediaItem
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@UnstableApi
class MediaPlayerController @Inject constructor(
    @ApplicationContext private val context: Context,
    token: SessionToken,
    private val controllerFuture: ListenableFuture<MediaController>,
) : Player.Listener {

    val isConnected: StateFlow<Boolean> get() = _isConnected
    val isPlaying: StateFlow<Boolean> get() = _isPlaying
    val currentPlayingAudio: StateFlow<MediaItem?> get() = _currentPlayingAudio

    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _currentPlayingAudio: MutableStateFlow<MediaItem?> = MutableStateFlow(null)

    private val browserFuture = MediaBrowser.Builder(context, token).buildAsync()
    private var mediaBrowser: MediaBrowser? = null
    private var mediaController: MediaController? = null
    private var audioList = MutableStateFlow<List<MediaItem>>(arrayListOf())

    init {
        browserFuture.addListener({
            mediaBrowser = browserFuture.get()
            connectToServer()
            _isConnected.value = true
        }, MoreExecutors.directExecutor())
    }

    fun disconnect() {
        mediaBrowser?.removeListener(this)
        mediaBrowser = null
    }

    fun playAudio(tracks: List<TrackModel>, audioId: String) {
        audioList.value = tracks.toListMediaItem()
        val index =
            audioList.value.indexOf(audioList.value.find { it.mediaId == audioId })
        mediaController?.run {
            clearMediaItems()
            addMediaItems(audioList.value)
            prepare()
            seekTo(index, 0)
            playWhenReady = true
        }
    }

    fun detectCurrentPosition(): Float = mediaController?.currentPosition?.toFloat() ?: 0F

    fun playTrack() {
        mediaController?.play()
    }

    fun pauseTrack() {
        mediaController?.pause()
    }

    fun seekTo(position: Long) {
        mediaController?.seekTo(
            position
        )
    }

    fun skipToNext() {
        mediaController?.seekToNextMediaItem()
    }

    fun skipToPrevious() {
        mediaController?.seekToPreviousMediaItem()
    }

    fun onShuffleMode() {
        mediaController?.shuffleModeEnabled = true
    }

    fun offShuffleMode() {
        mediaController?.shuffleModeEnabled = false
    }

    fun onRepeatMode() {
        mediaController?.repeatMode = Player.REPEAT_MODE_ONE
    }

    fun offRepeatMode() {
        mediaController?.repeatMode = Player.REPEAT_MODE_OFF
    }

    private fun connectToServer() {
        mediaBrowser?.run {
            if (isConnected) {
                controllerFuture.addListener({
                    mediaController = controllerFuture.get()
                }, MoreExecutors.directExecutor())
                mediaBrowser?.addListener(this@MediaPlayerController)
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        _isPlaying.value = isPlaying
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        _currentPlayingAudio.value = mediaItem?.let { item ->
            audioList.value.find {
                it.mediaId == item.mediaId
            }
        }
    }
}