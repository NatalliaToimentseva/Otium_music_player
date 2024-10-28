package com.example.otiummusicplayer.repository.dataSource.impl

import com.example.otiummusicplayer.models.networkPart.ArtistModel
import com.example.otiummusicplayer.repository.dataSource.BasePagingSource
import com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain.LoadArtistsUseCase
import javax.inject.Inject

class ArtistPagingSource @Inject constructor(
    private val useCase: LoadArtistsUseCase
) : BasePagingSource<ArtistModel>({ l, i ->
    useCase.loadArtist(l, i)
}
)