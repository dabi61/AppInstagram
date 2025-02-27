package com.example.appinstagram.model

data class Page(
    val page: Int,
    val totalPage: Int,
    val totalPost: Int,
    val data: List<HomeData.Post>
)