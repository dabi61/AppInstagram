package com.example.appinstagram.MyInterface

import com.example.appinstagram.model.HomeData

interface PostClick {
    fun onPostClick(post : HomeData.Post)
    fun onMorePostClick(post : HomeData.Post)
}