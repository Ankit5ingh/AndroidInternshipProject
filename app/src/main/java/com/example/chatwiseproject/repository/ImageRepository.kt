package com.example.chatwiseproject.repository

import com.example.chatwiseproject.api.RetrofitInstance
import com.example.chatwiseproject.model.ApiResponse
import com.example.chatwiseproject.model.ImageModel
import retrofit2.Response

class ImageRepository {
    suspend fun getImage() : Response<List<ImageModel>> = RetrofitInstance.api.getImage()
}