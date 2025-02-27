package com.example.appinstagram.extension

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun getImageMultipart(context: Context, imageUri: Uri?): MultipartBody.Part? {
    if (imageUri == null) return null
    val file = File(imageUri.path ?: return null)
    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
    return MultipartBody.Part.createFormData("avatar", file.name, requestFile)
}
