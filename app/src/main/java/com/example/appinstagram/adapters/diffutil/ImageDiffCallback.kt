package com.example.appinstagram.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil

object ImageDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem // Vì mỗi hình ảnh có URL duy nhất
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
