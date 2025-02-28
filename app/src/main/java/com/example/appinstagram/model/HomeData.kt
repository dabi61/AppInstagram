package com.example.appinstagram.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

sealed class HomeData {


    data class UserList(val userList: List<User>) : HomeData()
    @Parcelize
    data class Post(val _id : String,
                    val author: User,
                    val images : List<String>,
                    val content : String?,
                    val createdAt: String?,
                    val updatedAt: String?,
                    val listLike : List<User>,
                    var totalLike: Int, ) : HomeData(), Parcelable

    data class Null(var str: String = ""): HomeData()
}

