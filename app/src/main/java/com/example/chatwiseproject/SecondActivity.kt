package com.example.chatwiseproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatwiseproject.adapter.ImageAdapter
import com.example.chatwiseproject.repository.ImageRepository
import com.example.chatwiseproject.util.Resource

class SecondActivity : AppCompatActivity() {

    private val TAG : String = "SecondActivity"

    private lateinit var viewModel: ImageViewModel
    private lateinit var factory: ImageViewModelFactory
    private lateinit var rvImage : RecyclerView
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        initViewModel()
        setUpRecyclerView()
        Log.e(TAG, "Outside SetUpRecyclerView")

        viewModel.newImage.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
//                    hideProgressBar()
                    response.data?.let { apiResponse ->
                        imageAdapter.differ.submitList(apiResponse)
                        Log.e(TAG, "Success $apiResponse")
                    }
                }
                is Resource.Error -> {
//                    hideProgressBar()
                    response.message?.let {
                        Log.e(TAG, "An Error occurred $it")
                    }
                }
                is Resource.Loading -> {
//                    showProgressBar()
                }
            }
        }

    }

//    private fun hideProgressBar() {
//        paginationProgressBar.visibility = View.INVISIBLE
//    }
//    private fun showProgressBar() {
//        paginationProgressBar.visibility = View.VISIBLE
//    }

    private fun initViewModel() {
        Log.e(TAG, "Inside initViewModel")
        val repository = ImageRepository()
        factory = ImageViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ImageViewModel::class.java]
    }

    private fun setUpRecyclerView() {
        Log.e(TAG, "Inside SetUpRecyclerView")
        rvImage = findViewById(R.id.rvImage)
        imageAdapter = ImageAdapter()
        rvImage.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}

