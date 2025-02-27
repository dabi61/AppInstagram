package com.example.appinstagram.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinstagram.R
import com.example.appinstagram.adapters.diffutil.MyPostDiffCallback
import com.example.appinstagram.databinding.ItemMyPostBinding
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.userInterface.PostClick

class MyPostAdapter(
    private val context: Context,
    private val screenWidth: Int,
    private val spanCount: Int,
    private val listener: PostClick
) : ListAdapter<HomeData.Post, MyPostAdapter.MyPostViewHolder>(MyPostDiffCallback) { // Sửa cách gọi diffUtil

    inner class MyPostViewHolder(private val binding: ItemMyPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindMyPost(post: HomeData.Post) {
            binding.ivPost.setOnClickListener {
                listener.onPostClick(post)
            }
            if (post.images.isNotEmpty()) {
                Glide.with(context)
                    .load(post.images[0])
                    .into(binding.ivPost)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostViewHolder {
        val binding = ItemMyPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemWidth = screenWidth / spanCount
        binding.root.layoutParams = ViewGroup.LayoutParams(itemWidth, itemWidth)
        return MyPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPostViewHolder, position: Int) {
        holder.bindMyPost(getItem(position))
    }
}
