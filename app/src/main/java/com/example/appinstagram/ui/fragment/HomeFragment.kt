package com.example.appinstagram.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appinstagram.R
import com.example.appinstagram.adapters.HomeAdapter
import com.example.appinstagram.databinding.FragmentHomeBinding
import com.example.appinstagram.model.User
import com.example.appinstagram.utils.DataStatus
import com.example.appinstagram.viewmodel.MainViewModel
import com.example.instagram.userInterface.UserClick

import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel: MainViewModel by viewModel()
    lateinit var adapterHome: HomeAdapter
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        })
        setupRecyclerView()
        lifecycleScope.launch {
            viewModel.getAllPosts()
            viewModel.posts.observe(viewLifecycleOwner){
                Log.d("HomeFragment", "onCreateView: ${it.data}")

                when(it.status){
                    DataStatus.Status.LOADING -> {
                        showProgressBar(true)
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }
                    DataStatus.Status.SUCCESS -> {
                        showProgressBar(false)
                        adapterHome.submitList(it.data)

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