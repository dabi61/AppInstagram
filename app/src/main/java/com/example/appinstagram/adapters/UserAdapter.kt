package com.example.appinstagram.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinstagram.R
import com.example.appinstagram.databinding.ItemUserBinding
import com.example.appinstagram.model.User
import com.example.instagram.userInterface.UserClick

class UserAdapter(
    users: List<User>,
    private val username: String?,
    val context: Context,
    private val listener: UserClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val MY_ITEM = 0
        private const val USER_ITEM = 1
    }

    private var sortedUsers: List<User> = listOf()

    init {
        setSortedUsers(users)
    }

    private fun setSortedUsers(users: List<User>) {
        val myUser = users.find { it.username == username }
        val otherUsers = users.filter { it.username != username }
        sortedUsers = if (myUser != null) listOf(myUser) + otherUsers else users
    }

    inner class MyViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindUser(user: User) {
            binding.tvNameStory.text = "My Story"
            binding.ivImage.setOnClickListener {
                Toast.makeText(context, "Chưa phát hành tính năng này!", Toast.LENGTH_SHORT).show()
            }
            binding.ivAddStory.visibility = View.VISIBLE
            Glide.with(context)
                .load(user.avatar)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.ivImage)
        }
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
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
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && sortedUsers[position].username == username) MY_ITEM else USER_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == MY_ITEM) MyViewHolder(binding) else UserViewHolder(binding)
    }

    override fun getItemCount(): Int = sortedUsers.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = sortedUsers[position]
        when (holder) {
            is MyViewHolder -> holder.bindUser(user)
            is UserViewHolder -> holder.bindUser(user)
        }
    }

    fun updateList(newUsers: List<User>) {
        setSortedUsers(newUsers)
        notifyDataSetChanged()
    }
}
