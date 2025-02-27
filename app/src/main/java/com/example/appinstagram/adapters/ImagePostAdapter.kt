package com.example.appinstagram.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinstagram.R
import com.example.appinstagram.databinding.ItemImageBinding


class ImagePostAdapter(private val listPhoto: List<String>, val context: Context) : RecyclerView.Adapter<ImagePostAdapter.ImagePostViewHolder>() {
    inner class ImagePostViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: String) {
            Log.d("ImagePostAdapter", "bind: $photo")
            Glide.with(context)
                .load(photo)
                .error(R.drawable.background)
                .into(binding.ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePostViewHolder {
        return ImagePostViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listPhoto.size
    }

    override fun onBindViewHolder(holder: ImagePostViewHolder, position: Int) {
        val photo = listPhoto[position]
        holder.bind(photo)
    }
}
