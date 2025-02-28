package com.example.appinstagram.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.appinstagram.model.User

object ListLikeDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.username == newItem.username
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}