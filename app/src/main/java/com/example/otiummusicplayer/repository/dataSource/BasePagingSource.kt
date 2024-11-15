package com.example.otiummusicplayer.repository.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.otiummusicplayer.ui.features.networkPart.mainScreen.domain.PagingResult
import retrofit2.HttpException

private const val BASE_STARTING_PAGE_INDEX = 1
private const val ONE_PAGE = 1

abstract class BasePagingSource<T : Any>(
    private val request: suspend (limit: String, offset: Int) -> PagingResult<T>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(ONE_PAGE) ?: page.nextKey?.minus(ONE_PAGE)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page: Int = params.key ?: BASE_STARTING_PAGE_INDEX
        val pageSize = params.loadSize

        return try {
            val response = request(
                pageSize.toString(),
                page * pageSize
            )

            when (response) {
                is PagingResult.Success -> {
                    LoadResult.Page(
                        data = response.data,
                        prevKey = if (page == BASE_STARTING_PAGE_INDEX) null else page - ONE_PAGE,
                        nextKey = if (response.data.size < pageSize) null else page + ONE_PAGE
                    )
                }

                is PagingResult.Error -> {
                    LoadResult.Error(response.error)
                }
            }
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}