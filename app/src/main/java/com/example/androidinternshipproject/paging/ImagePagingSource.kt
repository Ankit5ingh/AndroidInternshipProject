package com.example.androidinternshipproject.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidinternshipproject.api.ImageApi
import com.example.androidinternshipproject.model.Photo

class ImagePagingSource(val imageApi : ImageApi) : PagingSource<Int, Photo>() {
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val pos = params.key?:1
            val responseX  = imageApi.getImage(page = pos)
            return LoadResult.Page(
                data = responseX.photos.photo,
                prevKey = if(pos == 1) null else pos -1,
                nextKey = if (pos == responseX.photos.pages) null else pos + 1
            )
        }catch (e : Exception){
            LoadResult.Error(e)
        }
    }

}