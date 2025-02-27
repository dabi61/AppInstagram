package com.example.appinstagram.userInterface

import com.example.appinstagram.model.HomeData

interface PostClick {
    fun onPostClick(post : HomeData.Post)
}