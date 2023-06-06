package com.example.androidinternshipproject.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidinternshipproject.R
import com.example.androidinternshipproject.model.Photo
import kotlinx.android.synthetic.main.list_item.view.*

class SearchImageAdapter : RecyclerView.Adapter<SearchImageAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newltem: Photo): Boolean {
                return oldItem.url_s == newltem.url_s
            }

            override fun areContentsTheSame(oldItem: Photo, newltem: Photo): Boolean {
                return oldItem == newltem
            }
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(image?.url_s)
                .placeholder(R.drawable.ic_baseline_cloud_off_24)
                .into(ivImage)
            tvTitle.text = image?.title
            setOnClickListener {
                onItemClickListener?.let { it (image!!) }
            }
        }
    }

    private var onItemClickListener : ((Photo) -> Unit)? = null

    fun setOnItemClickListener(listener : (Photo)-> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}