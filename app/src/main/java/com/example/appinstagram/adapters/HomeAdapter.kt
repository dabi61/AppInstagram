package com.example.appinstagram.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinstagram.MyInterface.PostClick
import com.example.appinstagram.R
import com.example.appinstagram.adapters.diffutil.HomeDiffCallback
import com.example.appinstagram.databinding.ItemPostBinding
import com.example.appinstagram.databinding.ListItemUserBinding
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.model.User
import com.example.appinstagram.utils.LikeValue
import com.example.instagram.userInterface.UserClick

class HomeAdapter(
    private val context: Context,
    private val listener: UserClick,
    private val listenerPost: PostClick
) : ListAdapter<HomeData, RecyclerView.ViewHolder>(HomeDiffCallback) {
    var imagePostAdapter: ImagePostAdapter? = null
    var posts: List<HomeData.Post>? = null
    var users: List<User>? = null

    companion object {
        private const val USER_ITEM = 0
        private const val POST_ITEM = 1
    }

    inner class UserViewHolder(val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindUserView(item: HomeData.UserList) {
            val adapterUser = UserAdapter(users!!, context, listener)
            binding.rvUser.apply {
                adapter = adapterUser
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
        }
    }

    inner class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindPostView(item: HomeData.Post) {
            val sharePref = context.getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val username = sharePref.getString("username", "")
            if (!item.author.username.equals(username)) {
                binding.ivMore.visibility = View.GONE
            }
            var status : LikeValue = LikeValue.UNLIKE
            item.listLike.forEach{
                Log.d("Hello", "aaaa: ${it.username}, ${username}")
                status = if (it.username == username) {
                    LikeValue.LIKE
                } else {
                    LikeValue.UNLIKE
                }
            }
            if (status == LikeValue.LIKE) {
                binding.ivLove.setImageResource(R.drawable.ic_heart_full)
            } else {
                binding.ivLove.setImageResource(R.drawable.ic_heart)
            }
            binding.ivLove.setOnClickListener {
                listenerPost.onLikeClick(item, status)
            }

            with(binding) {
                tvUsername.text = item.author.username
                tvContent.text = item.content
                tvNumLove.text = item.totalLike.toString()

                Glide.with(context)
                    .load(item.author.avatar)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(ivAvatar)

                imagePostAdapter = ImagePostAdapter(item.images, context)
                vpImage.adapter = imagePostAdapter
                ciImage.setViewPager(vpImage)

                ivAvatar.setOnClickListener {
                    listener.onAvatarClick(item.author)
                }
                ivMore.setOnClickListener {
                    listenerPost.onMorePostClick(item, it)
                }




                tvUsername.setOnClickListener {
                        listener.onAvatarClick(item.author)
                    }
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) USER_ITEM else POST_ITEM
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            posts = currentList.filterIsInstance<HomeData.Post>()
            users = posts?.map { it.author }
            users = users?.distinctBy { it.username }

            return when (viewType) {
                USER_ITEM -> {
                    val binding = ListItemUserBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                    UserViewHolder(binding)
                }

                POST_ITEM -> {
                    val binding =
                        ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    PostViewHolder(binding)
                }

                else -> throw IllegalArgumentException("Invalid view type")
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            Log.d("HomeAdapter", "Binding item at position: $position, Data: ${users}")
            if (position == 0) {
                val item = HomeData.UserList(users!!)
                (holder as UserViewHolder).bindUserView(item)
            } else {
                val item = getItem(position)
                (holder as PostViewHolder).bindPostView(item as HomeData.Post)
            }
        }
    }


