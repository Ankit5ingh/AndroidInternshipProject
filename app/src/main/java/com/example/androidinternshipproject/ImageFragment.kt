package com.example.androidinternshipproject


import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidinternshipproject.paging.ImageAdapter
import com.example.androidinternshipproject.paging.LoaderAdapter
import com.example.androidinternshipproject.util.Constants
import com.example.androidinternshipproject.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_image.*

class ImageFragment : Fragment(R.layout.fragment_image), OnTouchListener {

    private val TAG: String = "breakingNewsFragment"
    private lateinit var viewModel: ImageViewModel
    private lateinit var imageAdapter: ImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ( activity as MainActivity).viewModel
        setUpRecyclerView()

        viewModel.newImage.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        it.observe(viewLifecycleOwner) { list ->
                            imageAdapter.submitData(lifecycle, list)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        snackBar(it)
                        Log.e(TAG, "An Error occurred $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun snackBar(it : String) {
        Snackbar.make(rvImage, it, Snackbar.LENGTH_SHORT).apply {
            this.setAction("Retry") {
                if (!viewModel.hasInternetConnection()) {
                    Toast.makeText(
                        context,
                        "No Internet Connection",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.getImage()
                }
            }
        }.show()
    }



    override fun onTouch(v:View, event : MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                viewModel.getImage()
            }
            MotionEvent.ACTION_UP -> {
                viewModel.getImage()
            }
            MotionEvent.ACTION_MOVE -> {
                viewModel.getImage()
            }
        }
        return true
    }


    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                if(!viewModel.hasInternetConnection()){
                    showProgressBar()
                    snackBar("No Internet Connection")
                }
                hideProgressBar()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLastPage && !isLoading
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

        }
    }


    private fun setUpRecyclerView() {
        imageAdapter = ImageAdapter()
        rvImage.apply {
            setHasFixedSize(true)
            adapter = imageAdapter.withLoadStateHeaderAndFooter(
                header = LoaderAdapter(),
                footer = LoaderAdapter()
            )
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(this@ImageFragment.scrollListener)
        }
    }

}