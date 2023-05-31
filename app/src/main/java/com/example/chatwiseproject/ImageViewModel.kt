package com.example.chatwiseproject

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwiseproject.model.ApiResponse
import com.example.chatwiseproject.model.ImageModel
import com.example.chatwiseproject.repository.ImageRepository
import com.example.chatwiseproject.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ImageViewModel (private val repository: ImageRepository) : ViewModel() {

    val newImage: MutableLiveData<Resource<List<ImageModel>>> = MutableLiveData()
    var newImageResponse: List<ImageModel>? = null


    init {
        getImage()
    }

    private fun getImage() = viewModelScope.launch {
        newImage.postValue(Resource.Loading())
        val response = repository.getImage()
        Log.i("Response", response.message())
        newImage.postValue(handelImageResponse(response))
    }

    private fun handelImageResponse(response: Response<List<ImageModel>>) : Resource<List<ImageModel>>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse -> // instead of it we wrote resultResponse
                if(newImageResponse == null) {
                    newImageResponse = resultResponse
                }
//                }else{
//                    val oldArticles = newImageResponse?.images
//                    val newArticles = resultResponse.images
//                    oldArticles?.addAll(newArticles)
//                    newImageResponse?.images = oldArticles!!
//                }
                return Resource.Success(newImageResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}