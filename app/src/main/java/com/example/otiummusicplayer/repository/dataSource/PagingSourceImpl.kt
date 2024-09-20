package com.example.otiummusicplayer.repository.dataSource

import com.example.otiummusicplayer.models.AlbumModel
import com.example.otiummusicplayer.models.ArtistModel
import com.example.otiummusicplayer.ui.features.search.screens.main.LoadAlbumsUseCase
import com.example.otiummusicplayer.ui.features.search.screens.main.LoadArtistsUseCase
import javax.inject.Inject

class AlbumPagingSource @Inject constructor(
    private val useCase: LoadAlbumsUseCase
) : BasePagingSource<AlbumModel>({ l, i ->
    useCase.loadAlbums(l, i)
}
)

class ArtistPagingSource @Inject constructor(
    private val useCase: LoadArtistsUseCase
) : BasePagingSource<ArtistModel>({ l, i ->
    useCase.loadArtist(l, i)
}
)

//class AlbumByArtistPagingSource @AssistedInject constructor(
//    @Assisted private val id: String,
//    private val useCase: LoadAlbumByArtistUseCase
//) : BasePagingSource<AlbumModel>({ l, i ->
//    useCase.loadAlbumsByArtist(l, i, id)
//}
//) {
//
//    @AssistedFactory
//    interface Factory {
//
//        fun create(id: String): AlbumByArtistPagingSource
//    }
//}

