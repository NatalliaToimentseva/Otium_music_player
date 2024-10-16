package com.example.otiummusicplayer.appComponents.services.media.exoPlayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.otiummusicplayer.appComponents.services.media.domain.MusicDataController
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.utils.formatTimeToMls
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import javax.inject.Inject

typealias OnReadyListener = (Boolean) -> Unit

class MediaSource @Inject constructor(
    private val musicDataController: MusicDataController
) {

    var audioMediaMetaData: List<MediaMetadataCompat> = emptyList()
    private val onReadyListeners: MutableList<OnReadyListener> = mutableListOf()
    private var state: AudioSourceState = AudioSourceState.STATE_CREATED
        set(value) {
            if (
                value == AudioSourceState.STATE_CREATED ||
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

    fun asMediaSource(dataSource: CacheDataSource.Factory): ConcatenatingMediaSource { //return items suitable for exoplayer to play one by one
        val concatenatingMediaSource = ConcatenatingMediaSource()
        audioMediaMetaData.forEach { mediaMetadataCompat ->
            val mediaItem = MediaItem.fromUri(
                mediaMetadataCompat.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
            )
            val mediaSource = ProgressiveMediaSource
                .Factory(dataSource)
                .createMediaSource(mediaItem)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItem() =
        audioMediaMetaData.map { metaData -> //this function gives metadata to display by UI
            val description = MediaDescriptionCompat.Builder()
                .setMediaId(metaData.description.mediaId)
                .setTitle(metaData.description.title)
                .setMediaUri(metaData.description.mediaUri)
                .build()
            MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
        }.toMutableList()

    fun refresh() {
        onReadyListeners.clear()
        state = AudioSourceState.STATE_CREATED
    }

    private fun transformData(data: List<TrackModel>) {
        audioMediaMetaData = data.map { audio ->
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audio.id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.name)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artistName)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, audio.albumName)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audio.audio)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, audio.image)
                .putLong(
                    MediaMetadataCompat.METADATA_KEY_DURATION,
                    formatTimeToMls(audio.duration).toLong()
                )
                .build()
        }
        state = AudioSourceState.STATE_INITIALIZED
    }
}

enum class AudioSourceState {

    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR,
}