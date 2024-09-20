package com.example.otiummusicplayer.repository.dataSource.impl

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.otiummusicplayer.network.entities.ArtistData
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import retrofit2.HttpException
import javax.inject.Inject

private const val BASE_STARTING_PAGE_INDEX = 1
private const val ONE_PAGE = 1

class ArtistDataSource @Inject constructor(
    private val repository: PlayerNetworkRepository
) : PagingSource<Int, ArtistData>() {

    override fun getRefreshKey(state: PagingState<Int, ArtistData>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(ONE_PAGE) ?: page.nextKey?.minus(ONE_PAGE)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistData> {
        val page: Int = params.key ?: BASE_STARTING_PAGE_INDEX
        val pageSize = params.loadSize
        Log.d("AAA", "ArtistDataSource page = $page and pageSize = $pageSize")
        val response = repository.loadArtists(pageSize.toString(), page * pageSize)
        val albData = response.body()?.results ?: arrayListOf()

        return try {
            if (response.isSuccessful) {
                LoadResult.Page(
                    data = albData,
                    prevKey = if (page == BASE_STARTING_PAGE_INDEX) null else page - ONE_PAGE,
                    nextKey = if (albData.size < pageSize) null else page + ONE_PAGE
                )
            } else {
                LoadResult.Error(
                    throwable = Throwable(response.message())
                )
            }
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}