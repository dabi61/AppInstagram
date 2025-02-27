package com.example.appinstagram.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.appinstagram.model.HomeData

object PostDiffCallback : DiffUtil.ItemCallback<HomeData.Post>() {
    override fun areItemsTheSame(oldItem: HomeData.Post, newItem: HomeData.Post): Boolean {
        return oldItem._id == newItem._id // So sánh ID để xác định cùng một bài post
    }

    override fun areContentsTheSame(oldItem: HomeData.Post, newItem: HomeData.Post): Boolean {
        return oldItem == newItem // So sánh toàn bộ nội dung
    }
}
