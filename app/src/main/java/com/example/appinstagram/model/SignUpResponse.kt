package com.example.appinstagram.model

data class SignUpResponse(
    val status: Boolean,
    val data: Profile,
    val message: String
)