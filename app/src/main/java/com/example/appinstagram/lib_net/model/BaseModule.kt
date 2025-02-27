package com.example.appinstagram.lib_net.model

data class BaseModel<out T>(val errorCode: Int, val errorMsg: String, val data: T)