package com.example.chatwiseproject.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatwiseproject.R
import com.example.chatwiseproject.model.ImageModel
import kotlinx.android.synthetic.main.list_item.view.*

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
    private val differCallback = object : DiffUtil.ItemCallback<ImageModel>() {
        override fun areItemsTheSame(oldItem: ImageModel, newltem: ImageModel): Boolean {
            return oldItem.url == newltem.url
        }

        override fun areContentsTheSame(oldItem: ImageModel, newltem: ImageModel): Boolean {
            return oldItem == newltem
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
            Glide.with(this).load(image.url).into(ivImage)
            tvTitle.text = image.title
            setOnClickListener {
                onItemClickListener?.let { it (image) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener : ((ImageModel) -> Unit)? = null

    fun setOnItemClickListener(listener : (ImageModel)-> Unit){
        onItemClickListener = listener
    }

}