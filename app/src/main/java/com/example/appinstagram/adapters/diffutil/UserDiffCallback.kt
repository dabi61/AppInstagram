package com.example.appinstagram.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.appinstagram.model.User

object UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.username == newItem.username // So sánh ID để xác định cùng một user
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem // So sánh toàn bộ nội dung
    }
}