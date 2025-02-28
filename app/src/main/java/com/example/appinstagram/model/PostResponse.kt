package com.example.appinstagram.model

import com.example.appinstagram.model.HomeData.Post
import com.example.appinstagram.utils.LikeValue
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

data class PostDeleteResponse(
    val status: Boolean,
    val message: String,
)
data class PostDeleteRequest(
    val userId: String,
    val postId: String
)

data class LikePostRequest(
    val userId: String,
    val postId: String,
    val likeValue : Int
)