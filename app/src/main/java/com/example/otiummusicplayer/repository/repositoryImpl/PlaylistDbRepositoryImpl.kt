package com.example.otiummusicplayer.repository.repositoryImpl

import androidx.paging.PagingSource
import com.example.otiummusicplayer.repository.PlaylistDbRepository
import com.example.otiummusicplayer.roomDB.dao.PlaylistsDao
import com.example.otiummusicplayer.roomDB.entity.PlaylistsEntity
import javax.inject.Inject

class PlaylistDbRepositoryImpl @Inject constructor(
    private val dao: PlaylistsDao
) : PlaylistDbRepository {

    override suspend fun addPlaylist(playlistsEntity: PlaylistsEntity) =
        dao.createPlaylist(playlistsEntity)

    override suspend fun deletePlaylistById(id: List<Long>) = dao.deletePlaylist(id)

    override fun getPlaylists(): PagingSource<Int, PlaylistsEntity> = dao.getPlaylists()
}