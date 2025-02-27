package com.example.appinstagram.extension

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun String.toRequestBody(): RequestBody = this.toRequestBody("text/plain".toMediaTypeOrNull())
