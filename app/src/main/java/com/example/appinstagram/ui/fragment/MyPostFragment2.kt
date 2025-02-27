package com.example.appinstagram.ui.fragment

import DetailPostAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appinstagram.databinding.FragmentMyPost2Binding
import com.example.appinstagram.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class MyPostFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentMyPost2Binding
    private var username: String? = null
    private lateinit var adapterMyPost: DetailPostAdapter
    private val viewModel: MainViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPost2Binding.inflate(inflater, container, false)
        adapterMyPost = DetailPostAdapter(requireActivity())
        setupRecyclerView()
        viewModel.myPost.observe(viewLifecycleOwner) {
            Log.d("MyPostFragment2", "onCreateView: ${it.data}")
            adapterMyPost.submitList(it.data)
        }
        binding.tvUsername.text = username
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