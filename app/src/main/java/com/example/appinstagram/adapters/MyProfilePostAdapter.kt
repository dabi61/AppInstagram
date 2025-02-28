package com.example.appinstagram.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinstagram.adapters.diffutil.MyPostDiffCallback
import com.example.appinstagram.databinding.ItemMyPostBinding
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.MyInterface.PostClick

class MyProfilePostAdapter(
    private val context: Context,
    private val screenWidth: Int,
    private val spanCount: Int,
    private val listener: PostClick
) : ListAdapter<HomeData.Post, MyProfilePostAdapter.MyProfilePostViewHolder>(MyPostDiffCallback) { // Sửa cách gọi diffUtil

    inner class MyProfilePostViewHolder(private val binding: ItemMyPostBinding) : RecyclerView.ViewHolder(binding.root) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProfilePostViewHolder{
        val binding = ItemMyPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemWidth = screenWidth / spanCount
        binding.root.layoutParams = ViewGroup.LayoutParams(itemWidth, itemWidth)
        return MyProfilePostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyProfilePostViewHolder, position: Int) {
        holder.bindMyPost(getItem(position))
    }
}
