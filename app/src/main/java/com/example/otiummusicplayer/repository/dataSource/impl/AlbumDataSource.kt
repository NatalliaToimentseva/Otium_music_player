package com.example.otiummusicplayer.repository.dataSource.impl

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.otiummusicplayer.network.entities.AlbumData
import com.example.otiummusicplayer.repository.PlayerNetworkRepository
import retrofit2.HttpException
import javax.inject.Inject

class AlbumDataSource @Inject constructor(
    private val repository: PlayerNetworkRepository
) : PagingSource<Int, AlbumData>() {

    override fun getRefreshKey(state: PagingState<Int, AlbumData>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AlbumData> {
        val page: Int = params.key ?: 1
        val pageSize = params.loadSize
        Log.d("AAA", "AlbumDataSource page = $page and pageSize = $pageSize")
        val response = repository.loadAlbums(pageSize.toString(), page * pageSize)
        val artData = response.body()?.results ?: arrayListOf()

        return try {
            if (response.isSuccessful) {
                LoadResult.Page(
                    data = artData,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (artData.size < pageSize) null else page + 1
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
                  LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}