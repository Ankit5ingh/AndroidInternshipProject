package com.example.androidinternshipproject.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.navigation.ui.R
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_image.view.*
import kotlinx.android.synthetic.main.loader_item.view.*

class LoaderAdapter: LoadStateAdapter<LoaderAdapter.LoaderViewHolder>() {
    class LoaderViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val progressBar: ProgressBar = itemView.progressBar
        fun bind(loadState: LoadState){
            progressBar.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.androidinternshipproject.R.layout.loader_item, parent, false)
        return LoaderViewHolder(view)
    }

}