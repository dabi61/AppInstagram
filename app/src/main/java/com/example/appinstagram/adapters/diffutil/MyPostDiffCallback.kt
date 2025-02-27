package com.example.appinstagram.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.appinstagram.model.HomeData

object MyPostDiffCallback : DiffUtil.ItemCallback<HomeData.Post>() {
    override fun areItemsTheSame(oldItem: HomeData.Post, newItem: HomeData.Post): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: HomeData.Post, newItem: HomeData.Post): Boolean {
        return oldItem == newItem
    }
}