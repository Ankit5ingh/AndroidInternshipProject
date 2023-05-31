package com.example.chatwiseproject.api

import com.example.chatwiseproject.model.ApiResponse
import com.example.chatwiseproject.model.ImageModel
import retrofit2.Response
import retrofit2.http.GET

interface NewsApi {
    @GET("/photos")
    suspend fun getImage() : Response<List<ImageModel>>
}