package com.example.appinstagram.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val username: String?,
                val name: String?,
                val gender: String?,
                val avatar: String?,
                val address: String?,
                val introduce: String?): Parcelable

