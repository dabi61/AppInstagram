package com.example.appinstagram.MyInterface

import android.view.View
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.utils.LikeValue

interface PostClick {
    fun onPostClick(post : HomeData.Post)
    fun onMorePostClick(post : HomeData.Post, view : View)
    fun onLikeClick(post : HomeData.Post, status : LikeValue)
    fun onTvLikeClick(post : HomeData.Post)
}