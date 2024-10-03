package com.example.otiummusicplayer.roomDB.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.otiummusicplayer.models.networkPart.TrackModel

@Entity(
    tableName = "Tracks",
    indices = [
        Index("id", unique = true)
    ]
)
data class TracksDbEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("db_id")
    val dbId: Long,
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("track_name")
    val trackName: String,
    @ColumnInfo("track_image")
    val trackImage: String?,
    @ColumnInfo("track_audio")
    val trackAudio: String,
    @ColumnInfo("duration")
    val duration: Int,
    @ColumnInfo("album_name")
    val albumName: String,
    @ColumnInfo("album_id")
    val albumId: String?,
    @ColumnInfo("artist_name")
    val artistName: String,
    @ColumnInfo("audio_download")
    val audioDownload: String?,
    @ColumnInfo("is_download_allowed")
    val isDownloadAllowed: Boolean?,
    @ColumnInfo("share_url")
    val shareUrl: String?,
    @ColumnInfo("favorite")
    val isFavorite: Boolean?
)

fun TracksDbEntity.toTrackModel(): TrackModel {
    return TrackModel(
        id,
        trackName,
        trackImage,
        trackAudio,
        duration,
        albumName,
        albumId,
        artistName,
        audioDownload,
        isDownloadAllowed,
        shareUrl,
        isFavorite
    )
}

fun List<TracksDbEntity>.toListTrackModels(): List<TrackModel> {
    return this.map { item -> item.toTrackModel() }
}
