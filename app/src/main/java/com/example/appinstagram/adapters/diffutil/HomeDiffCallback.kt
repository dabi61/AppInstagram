package com.example.appinstagram.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.appinstagram.model.HomeData

object HomeDiffCallback : DiffUtil.ItemCallback<HomeData>() {
    override fun areItemsTheSame(oldItem: HomeData, newItem: HomeData): Boolean {
        return when {
            oldItem is HomeData.UserList && newItem is HomeData.UserList -> oldItem == newItem
            oldItem is HomeData.Post && newItem is HomeData.Post -> oldItem._id == newItem._id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: HomeData, newItem: HomeData): Boolean {
        return oldItem == newItem
    }
}
