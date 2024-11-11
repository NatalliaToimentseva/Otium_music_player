package com.example.otiummusicplayer.repository.dataSource.impl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.otiummusicplayer.appComponents.contentProviders.MobileStorageMusicProvider
import com.example.otiummusicplayer.models.TrackModel
import com.example.otiummusicplayer.models.mobilePart.toListTrackModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.net.MalformedURLException
import java.sql.SQLException

private const val BASE_STARTING_PAGE_INDEX = 0
private const val ONE_PAGE = 1

class MobileStorageFolderTracksPagingSource @AssistedInject constructor(
    @Assisted private val folderId: Int,
    private val mobileStorageMusicProvider: MobileStorageMusicProvider
) : PagingSource<Int, TrackModel>() {

    override fun getRefreshKey(state: PagingState<Int, TrackModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(ONE_PAGE) ?: page.nextKey?.minus(ONE_PAGE)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrackModel> {
        val page: Int = params.key ?: BASE_STARTING_PAGE_INDEX
        val pageSize = params.loadSize

        return try {
            val response = mobileStorageMusicProvider.getTracksByFolderId(
                folderId,
                pageSize,
                page * pageSize
            )
            LoadResult.Page(
                data = response.toListTrackModel(),
                prevKey = if (page == BASE_STARTING_PAGE_INDEX) null else page - ONE_PAGE,
                nextKey = if (response.size < pageSize) null else page + ONE_PAGE
            )
        } catch (e: SQLException) {
            LoadResult.Error(e)
        } catch (e: MalformedURLException) {
            LoadResult.Error(e)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(folderId: Int): MobileStorageFolderTracksPagingSource
    }
}