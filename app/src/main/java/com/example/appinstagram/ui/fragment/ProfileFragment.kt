package com.example.appinstagram.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.appinstagram.adapters.ProfileViewPagerAdapter
import com.example.appinstagram.databinding.FragmentProfileBinding
import com.example.appinstagram.model.Profile
import com.example.appinstagram.model.User
import com.example.appinstagram.ui.activity.EditProfileActivity
import com.example.appinstagram.ui.activity.LoginActivity
import com.example.appinstagram.utils.DataStatus
import com.example.appinstagram.viewmodel.LoginViewModel
import com.example.appinstagram.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val PROFILE = "profile"

class ProfileFragment : Fragment() {
    private val editProfileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val name = result.data?.getStringExtra("name")
            val avatar = result.data?.getStringExtra("avatar")
            if (name != null) {
                binding.tvName.text = name
                profile?.name = name
            }
            Glide.with(requireContext())
                .load(avatar)
                .into(binding.ivAvatar)
            profile?.avatar = avatar
        }
    }
    private var profile: Profile? = null
    private val viewModel: MainViewModel by activityViewModel()
    private lateinit var profileViewPagerAdapter: ProfileViewPagerAdapter
    private lateinit var binding: FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profile = it.getParcelable(PROFILE)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        with(binding) {
            tvUsername.text = profile?.username
            tvName.text = profile?.name
        }
        val user = User(profile?.username, profile?.name, profile?.gender, profile?.avatar, profile?.address, profile?.introduce )
        profileViewPagerAdapter = ProfileViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle, user)
        if (profile?.avatar != null) {
            Glide.with(requireContext())
                .load(profile?.avatar)
                .into(binding.ivAvatar)
        }
        viewModel.getMyPost(profile?.username.toString())
        viewModel.myPost.observe(viewLifecycleOwner) {
            when (it.status) {
                DataStatus.Status.LOADING -> {}
                DataStatus.Status.SUCCESS -> {
                    if (it.data?.size != null)
                    {
                        binding.tvNumPost.text = it.data.size.toString()
                    }
                }
                DataStatus.Status.ERROR -> {}
            }
        }
        val icons : List<Int> = profileViewPagerAdapter.icons
        binding.vpImage.adapter = profileViewPagerAdapter
        binding.icLogout.setOnClickListener{
            requireActivity().finishAffinity()
            requireActivity().overridePendingTransition(0, 0)
            val sharePref = requireActivity().getSharedPreferences("MyPrefs", 0)
            val editor = sharePref.edit()
            editor.putString("username", profile?.username)
            editor.putString("password", "")
            editor.putString("_id", profile?._id)
            editor.putString("status", 0.toString())
            editor.apply()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btEditProfile.setOnClickListener{
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra("profile", profile)
            editProfileLauncher.launch(intent)
        }
        TabLayoutMediator(binding.tlImage, binding.vpImage) { tab, position ->
            when(position) {
                0 -> tab.icon = ContextCompat.getDrawable(requireContext(), icons[0])
                1 -> tab.icon = ContextCompat.getDrawable(requireContext(), icons[1])
                2 -> tab.icon = ContextCompat.getDrawable(requireContext(), icons[2])
            }
        }.attach()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(profile: Profile) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PROFILE, profile)
                }
            }
    }
}