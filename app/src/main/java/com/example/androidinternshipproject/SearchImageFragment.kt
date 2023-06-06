package com.example.androidinternshipproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidinternshipproject.adapter.SearchImageAdapter
import com.example.androidinternshipproject.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search_image.*
import kotlinx.android.synthetic.main.fragment_search_image.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchImageFragment : Fragment(R.layout.fragment_search_image) {

    private lateinit var viewModel: ImageViewModel
    private lateinit var imageAdapter : SearchImageAdapter
    private val TAG = "searchViewFragment"
    private lateinit var text : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ( activity as MainActivity).viewModel

        setUpRecyclerView()
        var job : Job? = null
        etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(newText: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(10L)
                    newText?.let {
                        if (newText.isNotEmpty()){
                            text = newText
                            viewModel.getSearchedImage(newText)
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    newText?.let {
                        if (newText.isNotEmpty()){
                            viewModel.getSearchedImage(newText)
                        }
                    }
                }
                return true
            }

        })

        viewModel.searchImage.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { apiResponse ->
                        if(apiResponse.photos.photo.isEmpty()){
                            tv_search_image.visibility = View.VISIBLE
                        }else{
                            tv_search_image.visibility = View.INVISIBLE
                        }
                        imageAdapter.differ.submitList(apiResponse.photos.photo)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Snackbar.make(rvSearchImage, it, Snackbar.LENGTH_SHORT).apply {
                            this.setAction("Retry", View.OnClickListener {
                                if(!viewModel.hasInternetConnection()){
                                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                                }else{
                                    viewModel.getSearchedImage(etSearch.toString())
                                }
                            })
                        }.show()
                        Log.e(TAG, "An Error occurred $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }
    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        imageAdapter = SearchImageAdapter()
        rvSearchImage.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}