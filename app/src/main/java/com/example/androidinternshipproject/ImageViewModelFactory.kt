package com.example.androidinternshipproject

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidinternshipproject.repository.ImageRepository
import java.lang.IllegalArgumentException

class ImageViewModelFactory (
    val app : Application,
    private val repository: ImageRepository):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            return ImageViewModel(app, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}