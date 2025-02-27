package com.example.appinstagram.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import com.example.appinstagram.R
import com.example.appinstagram.databinding.ActivityMainBinding
import com.example.appinstagram.lib_base.base.BaseActivity
import com.example.appinstagram.model.Profile
import com.example.appinstagram.ui.fragment.AddFragment
import com.example.appinstagram.ui.fragment.HomeFragment
import com.example.appinstagram.ui.fragment.ProfileFragment

import com.example.appinstagram.ui.fragment.SearchFragment
import com.example.appinstagram.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var addFragment: AddFragment
    private lateinit var profileFragment: ProfileFragment
    private val viewModel: MainViewModel by viewModel()
    private var activeFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val profile = intent.getParcelableExtra<Profile>("profile") as Profile
        homeFragment = HomeFragment()
        searchFragment = SearchFragment()
        profileFragment = ProfileFragment.newInstance(profile)
        activeFragment = homeFragment


        // Thêm các fragment nhưng chỉ hiển thị homeFragment
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_main, homeFragment, "HOME")
            .add(R.id.fl_main, searchFragment, "SEARCH").hide(searchFragment)
            .add(R.id.fl_main, profileFragment, "PROFILE").hide(profileFragment)
            .commit()


        binding.bnvMenu.setOnItemSelectedListener { item ->
            // Xử lý chuyển Fragment
            when (item.itemId) {
                R.id.home_fragment -> switchFragment(homeFragment)
                R.id.search_fragment -> switchFragment(searchFragment)
                R.id.add_fragment -> {
                    if (supportFragmentManager.findFragmentByTag("AddFragment") == null) {
                        val addFragment = AddFragment()
                        addFragment.show(supportFragmentManager, "AddFragment")
                    } else {
                        Log.d("Fragment", "AddFragment đã hiển thị, không cần hiển thị lại")
                    }
                }
                R.id.profile_fragment -> switchFragment(profileFragment)
                else -> switchFragment(homeFragment)
            }
            true
        }
    }

    private fun switchFragment(fragment: Fragment) {
        if (fragment == activeFragment) return
    if(activeFragment == homeFragment) {
        viewModel.getAllPosts()
    }
        supportFragmentManager.beginTransaction()
            .hide(activeFragment!!)  // Ẩn fragment hiện tại
            .show(fragment)          // Hiển thị fragment mới
            .commit()

        activeFragment = fragment
    }


}