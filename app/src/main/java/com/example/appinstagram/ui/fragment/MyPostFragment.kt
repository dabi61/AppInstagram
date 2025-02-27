package com.example.appinstagram.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appinstagram.R
import com.example.appinstagram.adapters.MyPostAdapter
import com.example.appinstagram.databinding.FragmentMyPostBinding
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.model.User
import com.example.appinstagram.MyInterface.PostClick
import com.example.appinstagram.utils.DataStatus
import com.example.appinstagram.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel


private const val USER = "user"
class MyPostFragment : Fragment() {
    private var user: User? = null
    private val viewModel: MainViewModel by activityViewModel()
    private lateinit var adapterMyPost: MyPostAdapter
    private lateinit var binding: FragmentMyPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(USER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPostBinding.inflate(inflater, container, false)
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val spanCount = 3
        adapterMyPost = MyPostAdapter(requireActivity() ,screenWidth, spanCount, object :
            PostClick {
            override fun onPostClick(post: HomeData.Post) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_in_right,
                        R.anim.slide_out_right,
                        R.anim.slide_out_right,
                    )
                    .add(R.id.fl_home, MyPostFragment2())
                    .addToBackStack(null)
                    .commit()
            }

            override fun onMorePostClick(post: HomeData.Post) {
                TODO("Not yet implemented")
            }
        })
        setupRecyclerView(spanCount)
        lifecycleScope.launch {
            viewModel.getMyPost(user?.username.toString())
            viewModel.myPost.observe(viewLifecycleOwner) {
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

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            MyPostFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER, user)
                }
            }
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