package com.example.appinstagram.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinstagram.adapters.diffutil.PicturePostDiffCallback
import com.example.appinstagram.databinding.ItemPicturePostBinding

class PicturePostAdapter(val context: Context) : ListAdapter<Uri, PicturePostAdapter.PictureViewHolder>(PicturePostDiffCallback) {


    inner class PictureViewHolder(val binding: ItemPicturePostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri) {
            Glide.with(context)
                .load(uri)
                .into(binding.ivPicture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val binding = ItemPicturePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("PicturePostAdapter", "onCreateViewHolder called $currentList")
        return PictureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



}