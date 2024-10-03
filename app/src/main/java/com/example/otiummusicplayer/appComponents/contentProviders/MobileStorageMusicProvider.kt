package com.example.otiummusicplayer.appComponents.contentProviders

import com.example.otiummusicplayer.models.mobilePart.MobileStorageTrackModel
import com.example.otiummusicplayer.models.mobilePart.TracksFolders

interface MobileStorageMusicProvider {

    suspend fun getAllStorageTracks(limit: Int, offset: Int): List<MobileStorageTrackModel>

    suspend fun getFolders(): Set<TracksFolders>

    suspend fun getTracksByFolderId(folderId: Int): List<MobileStorageTrackModel>
}