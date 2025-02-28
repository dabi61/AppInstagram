package com.example.appinstagram.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appinstagram.R
import com.example.appinstagram.databinding.FragmentMyPostBinding
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.model.User
import com.example.appinstagram.MyInterface.PostClick
import com.example.appinstagram.adapters.MyProfilePostAdapter
import com.example.appinstagram.utils.DataStatus
import com.example.appinstagram.utils.LikeValue
import com.example.appinstagram.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel



class ProfilePostFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModel()
    private lateinit var adapterMyPost: MyProfilePostAdapter
    private lateinit var binding: FragmentMyPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profile = viewModel.profile.value?.data?.data
        Log.d("profile", profile.toString())
        val user = User(profile?.username, profile?.name, profile?.gender, profile?.avatar, profile?.address, profile?.introduce )
        binding = FragmentMyPostBinding.inflate(inflater, container, false)
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val spanCount = 3
        adapterMyPost = MyProfilePostAdapter(requireActivity() ,screenWidth, spanCount, object :
            PostClick {
            override fun onPostClick(post: HomeData.Post) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_in_right,
                        R.anim.slide_out_right,
                        R.anim.slide_out_right,
                    )
                    .add(R.id.fl_profile, MyProfilePostFragment2())
                    .addToBackStack(null)
                    .commit()
            }

            override fun onMorePostClick(post: HomeData.Post, view: View) {
                TODO("Not yet implemented")
            }

            override fun onLikeClick(post: HomeData.Post, status: LikeValue) {
                TODO("Not yet implemented")
            }

            override fun onTvLikeClick(post: HomeData.Post) {
                val bottomSheet = ListLoveFragment(post)
                bottomSheet.show(childFragmentManager, "UserBottomSheet")
            }
        })
        setupRecyclerView(spanCount)
        lifecycleScope.launch {
            viewModel.getMyProfile(user.username.toString())
            viewModel.profilePost.observe(viewLifecycleOwner) {
                Toast.makeText(context, "${it.status}", Toast.LENGTH_SHORT).show()
                when (it.status) {
                    DataStatus.Status.LOADING -> {
                        showProgressBar(true)
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }
                    DataStatus.Status.SUCCESS -> {
                        showProgressBar(false)
                        adapterMyPost.submitList(it.data?.data?.data)
                    }
                    DataStatus.Status.ERROR -> {
                        showProgressBar(true)
                        Toast.makeText(requireContext(), "There is someThing wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        return binding.root
    }
    fun setupRecyclerView(spanCount: Int){
        binding.rvMyPost.apply {
            adapter = adapterMyPost
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            setHasFixedSize(true)
        }
    }
    private fun showProgressBar(isShown : Boolean) {
        binding.apply {
            if (isShown) {
                pLoading.visibility = View.VISIBLE
                rvMyPost.visibility = View.GONE
            } else {
                pLoading.visibility = View.GONE
                rvMyPost.visibility = View.VISIBLE
            }
        }
    }
}