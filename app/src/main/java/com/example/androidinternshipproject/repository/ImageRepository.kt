package com.example.androidinternshipproject.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.androidinternshipproject.api.RetrofitInstance
import com.example.androidinternshipproject.model.ApiResponseX
import com.example.androidinternshipproject.paging.ImagePagingSource
import retrofit2.Response

class ImageRepository {
    fun getImage() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = {ImagePagingSource(RetrofitInstance.api)}
    ).liveData
//    suspend fun getImage(pageNo:Int) : Response<ApiResponseX> =
//        RetrofitInstance.api.getImage(pageNumber = pageNo)
    suspend fun searchApi(searchString:String) : Response<ApiResponseX> =
        RetrofitInstance.api.searchApi(search = searchString)
}