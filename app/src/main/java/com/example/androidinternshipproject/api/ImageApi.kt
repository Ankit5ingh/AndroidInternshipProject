package com.example.androidinternshipproject.api

import com.example.androidinternshipproject.model.ApiResponseX
import com.example.androidinternshipproject.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {
    @GET("/services/rest/?method=flickr.photos.getRecent")
    suspend fun getImage(
        @Query("per_page")
        perPage: Int = 20,
        @Query("page")
        page:Int,
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("format")
        format: String = "json",
        @Query("nojsoncallback")
        noJsonCallBack: Int = 1,
        @Query("extras")
        extras: String = "url_s"
    ) : ApiResponseX

    @GET("/services/rest/?method=flickr.photos.search")
    suspend fun searchApi(
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("format")
        format: String = "json",
        @Query("nojsoncallback")
        noJsonCallBack: Int = 1,
        @Query("extras")
        extras: String = "url_s",
        @Query("text")
        search: String
    ) : Response<ApiResponseX>

}