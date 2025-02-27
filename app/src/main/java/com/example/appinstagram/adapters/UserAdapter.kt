package com.example.appinstagram.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinstagram.R
import com.example.appinstagram.databinding.ItemUserBinding
import com.example.appinstagram.model.User
import com.example.instagram.userInterface.UserClick

class UserAdapter(
    private val users: List<User>,
    val context: Context,
    private val listener: UserClick
) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindUser(user: User) {
            binding.tvNameStory.text = user.username
            binding.ivImage.setOnClickListener {
                listener.onAvatarClick(user)
            }
            Glide.with(context)
                .load(user.avatar)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.ivImage)
        }
        fun bindMyUser(user: User) {
            binding.tvNameStory.text = user.username
            binding.ivImage.setOnClickListener {
                listener.onAvatarClick(user)
            }
            Glide.with(context)
                .load(user.avatar)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return users.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUser(users.elementAt(position))
        Log.d("HomeAdapter", "bindUserView: ${users.size}")
    }
}