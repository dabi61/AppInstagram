package com.example.appinstagram.ui.fragment

import DetailPostAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appinstagram.MyInterface.PostClick
import com.example.appinstagram.R
import com.example.appinstagram.databinding.FragmentMyPost2Binding
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.model.LikePostRequest
import com.example.appinstagram.model.PostDeleteRequest
import com.example.appinstagram.utils.DataStatus
import com.example.appinstagram.utils.LikeValue
import com.example.appinstagram.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel



class MyProfilePostFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentMyPost2Binding
    private lateinit var adapterMyPost: DetailPostAdapter
    private val viewModel: MainViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPost2Binding.inflate(inflater, container, false)
        val _id = viewModel.profile.value?.data?.data?._id
        val username = viewModel.profile.value?.data?.data?.username
        adapterMyPost = DetailPostAdapter(requireActivity(), username, object : PostClick {

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
                            val request = PostDeleteRequest(_id.toString(), post._id)
                            viewModel.deletePost(request)
                            viewModel.deletePost.observe(viewLifecycleOwner) {
                                when(it.status) {
                                    DataStatus.Status.LOADING -> {}
                                    DataStatus.Status.SUCCESS -> {
                                        if (it.data?.status == true) {
                                            Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT).show()
                                            viewModel.getMyProfile(username.toString())
                                        }
                                    }
                                    DataStatus.Status.ERROR -> {}
                                }
                            }
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }

            override fun onLikeClick(post: HomeData.Post, status: LikeValue) {
                val request = LikePostRequest(_id.toString(), post._id, status.value)
                viewModel.likePost(request)
                viewModel.likePost.observe(viewLifecycleOwner) {
                    when(it.status) {
                        DataStatus.Status.LOADING -> {}
                        DataStatus.Status.SUCCESS -> {
                            if (it.data?.status == true) {
                                Toast.makeText(context, "${it.data.message}", Toast.LENGTH_SHORT).show()
                                viewModel.getAllPosts()
                            }
                        }
                        DataStatus.Status.ERROR -> {}
                    }
                }
            }

            override fun onTvLikeClick(post: HomeData.Post) {
                val bottomSheet = ListLoveFragment(post)
                bottomSheet.show(childFragmentManager, "UserBottomSheet")
            }



            override fun onPostClick(post: HomeData.Post) {

            }
        })
        setupRecyclerView()
        viewModel.profilePost.observe(viewLifecycleOwner) {
            Log.d("MyPostFragment2", "onCreateView: ${it.data}")
            adapterMyPost.submitList(it.data?.data?.data)
        }
        binding.tvUsername.text = viewModel.profile.value?.data?.data?.username
        binding.icBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()

        }
        return binding.root
    }
    fun setupRecyclerView(){
        binding.rvPost.apply {
            adapter = adapterMyPost
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
}