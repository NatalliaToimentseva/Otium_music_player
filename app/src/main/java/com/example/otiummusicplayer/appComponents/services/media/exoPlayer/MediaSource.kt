package com.example.otiummusicplayer.appComponents.services.media.exoPlayer

import androidx.annotation.OptIn
import com.example.otiummusicplayer.appComponents.services.media.domain.MusicDataController
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.utils.formatTimeToMls
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import javax.inject.Inject

typealias OnReadyListener = (Boolean) -> Unit

class MediaSource @Inject constructor(
    private val musicDataController: MusicDataController
) {
    var audioMediaItems : List<MediaItem> = arrayListOf()
    private val onReadyListeners: MutableList<OnReadyListener> = mutableListOf()
    private var state: AudioSourceState = AudioSourceState.STATE_CREATED
        set(value) {
            if (
                value == AudioSourceState.STATE_INITIALIZED ||
                value == AudioSourceState.STATE_ERROR
            ) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener: OnReadyListener ->
                        listener.invoke(isReady)
                    }
                }
            } else {
                field = value
            }
        }
    private val isReady: Boolean
        get() = state == AudioSourceState.STATE_INITIALIZED

    fun whenReady(listener: OnReadyListener): Boolean {
        return if (
            state == AudioSourceState.STATE_CREATED ||
            state == AudioSourceState.STATE_INITIALIZING
        ) {
            onReadyListeners += listener
            false
        } else {
            listener.invoke(isReady)
            true
        }
    }

    suspend fun load() {
        state = AudioSourceState.STATE_INITIALIZING
        musicDataController.listenCurrentMusicList().collect { list ->
            transformData(list)
        }
    }

    fun refresh() {
        onReadyListeners.clear()
        state = AudioSourceState.STATE_CREATED
    }

    @OptIn(UnstableApi::class)
    private fun transformData(data: List<TrackModel>) {
        audioMediaItems = data.map { audio ->
            buildMediaItem(audio)
        }
        state = AudioSourceState.STATE_INITIALIZED
    }

    @OptIn(UnstableApi::class)
    private fun buildMediaItem(audio: TrackModel): MediaItem {
        return MediaItem.Builder()
            .setMediaId(audio.id)
            .setUri(audio.audio)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(audio.name)
                    .setArtist(audio.artistName)
                    .setAlbumTitle(audio.albumName)
//                        .setArtworkData()
                    .setDurationMs(formatTimeToMls(audio.duration).toLong())
                    .build()
            )
            .build()
    }
}

enum class AudioSourceState {

    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR,
}