package com.example.appinstagram.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appinstagram.R
import com.example.appinstagram.model.User
import com.example.appinstagram.ui.fragment.MyPostFragment
import com.example.appinstagram.ui.fragment.MyRealsFragment
import com.example.appinstagram.ui.fragment.MyTagFragment

class ProfileViewPagerAdapter(fragmentManager : FragmentManager, lifecycle : Lifecycle, val user: User) : FragmentStateAdapter(fragmentManager, lifecycle) {
    val icons : List<Int> = listOf(
        R.drawable.ic_my_post,
        R.drawable.ic_reals,
        R.drawable.ic_profile
    )
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyPostFragment.newInstance(user)
            1 -> MyRealsFragment()
            2 -> MyTagFragment()
            else -> MyPostFragment.newInstance(user)
        }
    }
}