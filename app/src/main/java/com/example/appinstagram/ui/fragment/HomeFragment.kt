package com.example.appinstagram.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.copy
import com.example.appinstagram.MyInterface.PostClick
import com.example.appinstagram.R
import com.example.appinstagram.adapters.HomeAdapter
import com.example.appinstagram.databinding.FragmentHomeBinding
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.model.LikePostRequest
import com.example.appinstagram.model.PostDeleteRequest
import com.example.appinstagram.model.User
import com.example.appinstagram.utils.DataStatus
import com.example.appinstagram.utils.LikeValue
import com.example.appinstagram.viewmodel.MainViewModel
import com.example.instagram.userInterface.UserClick

import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.util.Date

class HomeFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModel()
    lateinit var adapterHome: HomeAdapter
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HomeFragment", "cre: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d("HomeFragment", "onPause: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllPosts()

    }

    override fun onResume() {
        super.onResume()
        Log.d("HomeFragment", "re: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val username = viewModel.profile.value?.data?.data?.username
        val profile = viewModel.profile.value?.data?.data
        adapterHome = HomeAdapter(requireActivity(), username, object : UserClick {
            override fun onAvatarClick(user: User) {
                val detailProfileFragment = DetailProfileFragment.newInstance(user)
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_in_right,
                        R.anim.slide_out_right,
                        R.anim.slide_out_right,
                    )
                    .add(R.id.fl_home, detailProfileFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }, object : PostClick {
            override fun onPostClick(post: HomeData.Post) {

            }

            override fun onMorePostClick(post: HomeData.Post, view: View) {
                val popup = PopupMenu(context, view)
                popup.menuInflater.inflate(R.menu.more_menu, popup.menu)

                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_edit -> {
                            Toast.makeText(context, "Chỉnh sửa", Toast.LENGTH_SHORT).show()
                            true
                        }

                        R.id.action_delete -> {
                            val sharePref =
                                requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            val _id = sharePref.getString("_id", "")
                            val request = PostDeleteRequest(_id.toString(), post._id)
//                            Log.d("HomeFragment", "Dữ liệu mới nhận được: ${_id}")
//                            Log.d("HomeFragment", "Dữ liệu mới nhận được: ${post._id}")
                            viewModel.deletePost(request)
                            viewModel.deletePost.observe(viewLifecycleOwner) {
                                if (it.data?.status == true) {
//                                    Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT).show()
                                    viewModel.getAllPosts()
                                }
                            }
                            true
                        }

                        else -> false
                    }
                }
                popup.show()
            }

            var liked: Boolean? = null
            override fun onLikeClick(post: HomeData.Post, status: LikeValue) {
                val sharePref = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE)
                val _id = sharePref.getString("_id", "")
                Log.d("HomeFragment", "Dữ liệu mới nhận được: ${status.value}")
                val request = LikePostRequest(_id.toString(), post._id, status.value)
                if (status == LikeValue.LIKE) {
                    liked = true
                } else {
                    liked = false
                }
                viewModel.likePost(request)
                viewModel.likePost.observe(viewLifecycleOwner) {
                    if (it.data?.status == true) {
                        Toast.makeText(context, "${it.data.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onTvLikeClick(post: HomeData.Post) {
                var newListLike: MutableList<User> = mutableListOf()
                val myUser: User = User(
                    profile?.username,
                    profile?.name,
                    profile?.gender,
                    profile?.avatar,
                    profile?.address,
                    profile?.introduce
                )
                newListLike = post.listLike.toMutableList()

                if (liked == true && !newListLike.contains(myUser)) {
                    newListLike.add(myUser)
                } else if (liked == false && newListLike.contains(myUser) && newListLike.size == post.totalLike) {
                    newListLike.remove(myUser)
                }
                val newPost: HomeData.Post = HomeData.Post(
                    post._id,
                    post.author,
                    post.images,
                    post.content,
                    post.createdAt,
                    post.updatedAt,
                    newListLike,
                    post.totalLike
                )
                val bottomSheet = ListLoveFragment(newPost)
                bottomSheet.show(childFragmentManager, "UserBottomSheet")
            }


        })
        setupRecyclerView()
        viewModel.posts.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "Dữ liệu mới nhận được: ${it.data}")
            val data: MutableList<HomeData> = mutableListOf()
            data.add(HomeData.Null())
            it.data?.let { it1 -> data.addAll(it1) }
            when (it.status) {
                DataStatus.Status.LOADING -> showProgressBar(true)
                DataStatus.Status.SUCCESS -> {
                    showProgressBar(false)
                    data.addAll(data)
                    adapterHome.submitList(data)
                }

                DataStatus.Status.ERROR -> {
                    showProgressBar(false)
                    Toast.makeText(requireContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show()

                }
            }
        }

        return binding.root
    }

    fun setupRecyclerView() {
        binding.rvPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            setItemViewCacheSize(50)
            adapter = adapterHome
        }
    }

    private fun showProgressBar(isShown: Boolean) {
        binding.apply {
            if (isShown) {
                pLoading.visibility = View.VISIBLE
                rvPost.visibility = View.GONE
            } else {
                pLoading.visibility = View.GONE
                rvPost.visibility = View.VISIBLE
            }
        }
    }
}