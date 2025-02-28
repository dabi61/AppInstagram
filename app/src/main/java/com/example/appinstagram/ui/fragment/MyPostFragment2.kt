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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appinstagram.MyInterface.PostClick
import com.example.appinstagram.R
import com.example.appinstagram.databinding.FragmentMyPost2Binding
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.model.PostDeleteRequest
import com.example.appinstagram.utils.LikeValue
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
    ): View {
        binding = FragmentMyPost2Binding.inflate(inflater, container, false)
        adapterMyPost = DetailPostAdapter(requireActivity(), object : PostClick {

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
                            val sharePref = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            val _id = sharePref.getString("_id", "")
                            val username = sharePref.getString("username", "")
                            val request = PostDeleteRequest(_id.toString(), post._id)
                            viewModel.deletePost(request)
                            viewModel.deletePost.observe(viewLifecycleOwner) {
                                if (it.data?.status == true) {
                                    Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT).show()
                                    viewModel.getAllPosts()
                                    viewModel.getMyPost(username.toString())
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
                TODO("Not yet implemented")
            }

            override fun onPostClick(post: HomeData.Post) {
                TODO("Not yet implemented")
            }
        })
        setupRecyclerView()
        viewModel.myPost.observe(viewLifecycleOwner) {
            Log.d("MyPostFragment2", "onCreateView: ${it.data}")
            adapterMyPost.submitList(it.data?.data?.data)
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