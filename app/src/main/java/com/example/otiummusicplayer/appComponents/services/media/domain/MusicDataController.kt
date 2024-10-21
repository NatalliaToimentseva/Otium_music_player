package com.example.otiummusicplayer.appComponents.services.media.domain

import com.example.otiummusicplayer.models.TrackModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicDataController @Inject constructor() {

    val currentMusicData = MutableSharedFlow<List<TrackModel>>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun listenCurrentMusicList(): Flow<List<TrackModel>> = currentMusicData

    val currentPosition = MutableSharedFlow<TrackModel>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun listenCurrentPosition(): Flow<TrackModel> = currentPosition
}