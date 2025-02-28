package com.example.appinstagram.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.appinstagram.R
import com.example.appinstagram.adapters.ProfileViewPagerAdapter
import com.example.appinstagram.databinding.FragmentDetailProfileBinding
import com.example.appinstagram.model.User
import com.example.appinstagram.utils.DataStatus
import com.example.appinstagram.viewmodel.MainViewModel

import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PROFILE = "profile"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailProfileFragment : Fragment() {
    private var profile: User? = null
    private val viewModel: MainViewModel by viewModel()
    private lateinit var profileViewPagerAdapter: ProfileViewPagerAdapter

    private lateinit var binding: FragmentDetailProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profile = it.getParcelable(PROFILE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailProfileBinding.inflate(inflater, container, false)
        profileViewPagerAdapter = ProfileViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle, profile!!)
        binding.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            setFragmentResult("DetailFragmentBack", bundleOf("reload" to true))
        }
        val icons : List<Int> = profileViewPagerAdapter.icons
        binding.vpImage.adapter = profileViewPagerAdapter
        TabLayoutMediator(binding.tlImage, binding.vpImage) { tab, position ->
            when(position) {
                0 -> tab.icon = ContextCompat.getDrawable(requireContext(), icons.get(0) )
                1 -> tab.icon = ContextCompat.getDrawable(requireContext(), icons.get(1))
                2 -> tab.icon = ContextCompat.getDrawable(requireContext(), icons.get(2))
            }
        }.attach()
        viewModel.getMyPost(profile?.username.toString())
        viewModel.myPost.observe(viewLifecycleOwner) {
            when (it.status) {
                DataStatus.Status.LOADING -> {}
                DataStatus.Status.SUCCESS -> {
                    if (it.data?.data?.data != null)
                    {
                        binding.tvNumPost.text = it.data.data.totalPost.toString()
                    }
                }
                DataStatus.Status.ERROR -> {}
            }
        }
        with(binding){
            tvUsername.text = profile?.username
            tvName.text = profile?.name
        }
        Glide.with(requireContext())
            .load(profile?.avatar)
            .placeholder(R.drawable.ic_profile)
            .into(binding.ivAvatar)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user: User) : DetailProfileFragment {
            val fragment = DetailProfileFragment()
            val args = Bundle()
            args.putParcelable(PROFILE, user)
            fragment.arguments = args
            return fragment
            }
    }
}