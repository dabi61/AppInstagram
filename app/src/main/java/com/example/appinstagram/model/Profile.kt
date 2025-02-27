package com.example.appinstagram.model

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Parcelize
data class Profile (
    var username: String,
    val password: String,
    var name: String,
    var gender: String?,
    var avatar: String?,
    var address: String?,
    var introduce: String?,
    val _id: String
) : Parcelable

data class LoginRequest(
    val username: String,
    val password: String
)

data class SignUpRequest(
    val username: String,
    val password: String,
    val name: String
)


data class UpdateProfileRequest(
    val name: RequestBody?,
    val avatar: MultipartBody.Part?,  // Ảnh đại diện
    val gender: RequestBody?,
    val address: RequestBody?,
    val introduce: RequestBody?,
    val userId: RequestBody
)

data class ChangePassRequest(
    val old_password: RequestBody?,
    val new_password: RequestBody?,
    val userId: RequestBody
)




