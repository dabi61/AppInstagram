package com.example.appinstagram.repositories

import com.example.appinstagram.model.HomeData
import com.example.appinstagram.model.LoginRequest
import com.example.appinstagram.model.Profile
import com.example.appinstagram.model.SignUpRequest
import com.example.appinstagram.model.SignUpResponse
import com.example.appinstagram.utils.DataStatus
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(request: LoginRequest) : Flow<DataStatus<Profile>>
    suspend fun signUp(request: SignUpRequest) : Flow<DataStatus<SignUpResponse>>

}