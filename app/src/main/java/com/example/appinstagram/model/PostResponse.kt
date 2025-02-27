package com.example.appinstagram.model

import com.example.appinstagram.model.HomeData.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody

data class PostResponse(
    val status: String,
    val data: Post,
    val message: String
)

data class PostRequest(
    val userId: RequestBody,
    val images: List<MultipartBody.Part?>,
    val content: RequestBody
)