package com.example.androidinternshipproject

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidinternshipproject.model.ApiResponseX
import com.example.androidinternshipproject.model.Photo
import com.example.androidinternshipproject.repository.ImageRepository
import com.example.androidinternshipproject.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ImageViewModel (
    app : Application,
    private val repository: ImageRepository) : AndroidViewModel(app) {

    val newImage: MutableLiveData<Resource<LiveData<PagingData<Photo>>>> = MutableLiveData()


    val searchImage : MutableLiveData<Resource<ApiResponseX>> = MutableLiveData()
    var searchImageResponse: ApiResponseX? = null

    init {
        getImage()
    }

    fun getImage() = viewModelScope.launch {
        safeApiCall()
    }


    fun getSearchedImage(searchQuery : String) = viewModelScope.launch {
        safeSearchApiCall(searchQuery)
    }

    private fun handelSearchImageResponse(response: Response<ApiResponseX>): Resource<ApiResponseX> {
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                    searchImageResponse = resultResponse
                return Resource.Success(searchImageResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handelImageResponse(response: Response<LiveData<PagingData<Photo>>>) : Resource<LiveData<PagingData<Photo>>>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeApiCall() {
        newImage.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = Response.success(repository.getImage().cachedIn(viewModelScope))
                newImage.postValue(handelImageResponse(response))
            }else{
                newImage.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> newImage.postValue(Resource.Error("Network Failure"))
                else -> newImage.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeSearchApiCall(searchQuery: String){
        searchImage.postValue(Resource.Loading())

        try {
            if(hasInternetConnection()) {
                val response = repository.searchApi(searchQuery)
                searchImage.postValue(handelSearchImageResponse(response))
            }else{
                searchImage.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> searchImage.postValue(Resource.Error("Network Failure"))
                else -> searchImage.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun hasInternetConnection() : Boolean {
        val connectivityManager = getApplication<ApiApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> true
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}