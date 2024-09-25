package com.example.otiummusicplayer.repository.dataSource

import com.example.otiummusicplayer.models.AlbumModel
import com.example.otiummusicplayer.models.ArtistModel
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.ui.features.search.main.LoadAlbumsUseCase
import com.example.otiummusicplayer.ui.features.search.main.LoadArtistsUseCase
import com.example.otiummusicplayer.ui.features.search.main.SearchTracksUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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

class SearchPagingSource @AssistedInject constructor(
    @Assisted private val query: String,
    private val useCase: SearchTracksUseCase
) : BasePagingSource<TrackModel>({ l, i ->
    useCase.searchTrackByQuery(query, l, i)
}
) {
        @AssistedFactory
    interface Factory {

        fun create(query: String): SearchPagingSource
    }
}

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

