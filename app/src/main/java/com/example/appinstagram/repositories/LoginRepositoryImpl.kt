package com.example.appinstagram.repositories

import com.example.appinstagram.api.ApiService
import com.example.appinstagram.model.LoginRequest
import com.example.appinstagram.model.SignUpRequest
import com.example.appinstagram.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LoginRepositoryImpl(private val apiService: ApiService) {


    suspend fun login(request: LoginRequest) = flow{
        emit(DataStatus.loading())
        val result = apiService.login(request)
        when(result.code()){
            200 -> emit(DataStatus.success(result.body()?.data))
            400 -> emit(DataStatus.error(result.message()))
            500 -> emit(DataStatus.error(result.message()))
        }
    }
        .catch { emit(DataStatus.error(it.message.toString())) }
        .flowOn(Dispatchers.IO)


    suspend fun signUp(request: SignUpRequest) = flow{
        emit(DataStatus.loading())
        val result = apiService.signUp(request)
        when(result.code()){
            200 -> emit(DataStatus.success(result.body()))
            400 -> emit(DataStatus.error(result.message()))
            500 -> emit(DataStatus.error(result.message()))
        }
    }
        .catch { emit(DataStatus.error(it.message.toString())) }
        .flowOn(Dispatchers.IO)
}