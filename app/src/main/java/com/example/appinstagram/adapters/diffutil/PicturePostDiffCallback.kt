package com.example.appinstagram.adapters.diffutil

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil

object PicturePostDiffCallback : DiffUtil.ItemCallback<Uri>() {
    override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem  // So sánh ID bài post trực tiếp
    }

    override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem  // Nếu ID giống nhau thì nội dung cũng giống nhau
    }
}
