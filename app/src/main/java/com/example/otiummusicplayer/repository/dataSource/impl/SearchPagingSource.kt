package com.example.otiummusicplayer.repository.dataSource.impl

import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.repository.dataSource.BasePagingSource
import com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain.SearchTracksUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

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