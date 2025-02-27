package com.example.appinstagram.model

import java.util.Date

sealed class HomeData {


    data class UserList(val userList: List<User>) : HomeData()

    data class Post(val _id : String,
                    val author: User,
                    val images : List<String>,
                    val content : String,
                    val createAt: Date,
                    val updateAt: Date,
                    val listLike : Any,
                    val totalLike: Int, ) : HomeData()

}

