package com.example.otiummusicplayer.repository.dataSource.impl

import com.example.otiummusicplayer.models.networkPart.AlbumModel
import com.example.otiummusicplayer.repository.dataSource.BasePagingSource
import com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain.LoadAlbumsUseCase
import javax.inject.Inject

class AlbumPagingSource @Inject constructor(
    private val useCase: LoadAlbumsUseCase
) : BasePagingSource<AlbumModel>({ l, i ->
    useCase.loadAlbums(l, i)
}
)