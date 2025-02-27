package com.example.appinstagram.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
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
import com.example.appinstagram.model.User
import com.example.appinstagram.utils.DataStatus
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("bottom_sheet_dismiss") { _, bundle ->
            val shouldReload = bundle.getBoolean("reload", false)
            if (shouldReload) {
                viewModel.getAllPosts() // Gọi lại API hoặc cập nhật dữ liệu
            }
        }

    }
    override fun onResume() {
        super.onResume()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapterHome = HomeAdapter(requireActivity(),object : UserClick {
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
                TODO("Not yet implemented")
            }

            override fun onMorePostClick(post: HomeData.Post) {
                val popup = PopupMenu(requireContext(), view)
                popup.menuInflater.inflate(R.menu.more_menu, popup.menu)

                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_edit -> {
                            Toast.makeText(context, "Chỉnh sửa", Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.action_delete -> {
                            val sharePref = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            val _id = sharePref.getString("_id", "")
                            viewModel.deletePost(post._id, _id.toString())
                            viewModel.deletePost.observe(viewLifecycleOwner) {
                                Log.d("HomeFragment", "Dữ liệu mới nhận được: ${it}")
                                Toast.makeText(context, "${it.data?.message}", Toast.LENGTH_SHORT).show()
                            }
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }

        })
        setupRecyclerView()
        viewModel.getAllPosts()
        viewModel.posts.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "Dữ liệu mới nhận được: ${it.data}")
            when (it.status) {
                DataStatus.Status.LOADING -> showProgressBar(true)
                DataStatus.Status.SUCCESS -> {
                    showProgressBar(false)
                    adapterHome.submitList(it.data) {
                        Log.d("HomeFragment", "Danh sách bài viết đã cập nhật")
                    }
                }
                DataStatus.Status.ERROR -> {
                    showProgressBar(false)
                    Toast.makeText(requireContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    fun setupRecyclerView(){
        binding.rvPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            setItemViewCacheSize(50)
            adapter = adapterHome
        }
    }
    private fun showProgressBar(isShown : Boolean) {
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