package com.example.otiummusicplayer.repository.dataSource.impl

//class AlbumByArtistDataSource @AssistedInject constructor(
//    @Assisted private val id: String,
//    private val repository: PlayerNetworkRepository
//) : PagingSource<Int, AlbumData>() {
//
//    override fun getRefreshKey(state: PagingState<Int, AlbumData>): Int? {
//        val anchorPosition = state.anchorPosition ?: return null
//        val page = state.closestPageToPosition(anchorPosition) ?: return null
//        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AlbumData> {
//        val page: Int = params.key ?: 1
//        val pageSize = params.loadSize
//        Log.d("AAA", "AlbumByArtistDataSource page = $page and pageSize = $pageSize")
//        val response = repository.loadAlbumsByArtistId(
//            pageSize.toString(),
//            page * pageSize,
//            id = id
//        )
//        val albData = response.body()?.results ?: arrayListOf()
//
//        return try {
//            if (response.isSuccessful) {
//                LoadResult.Page(
//                    data = albData,
//                    prevKey = if (page == 1) null else page - 1,
//                    nextKey = if (albData.size < pageSize) null else page + 1
//                )
//            } else {
//                LoadResult.Error(
//                    throwable = Throwable(response.message())
//                )
//            }
//        } catch (e: HttpException) {
//            LoadResult.Error(e)
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    @AssistedFactory
//    interface Factory {
//
//        fun create(id: String): AlbumByArtistDataSource
//    }
//}