package com.example.appinstagram.repositories

import android.util.Log
import com.example.appinstagram.api.ApiService
import com.example.appinstagram.model.ChangePassRequest
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.model.LikePostRequest
import com.example.appinstagram.model.PostDeleteRequest
import com.example.appinstagram.model.PostDeleteResponse
import com.example.appinstagram.model.PostRequest
import com.example.appinstagram.model.PostResponse
import com.example.appinstagram.model.Profile
import com.example.appinstagram.model.UpdateProfileRequest
import com.example.appinstagram.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainRepositoryImpl(private val apiService: ApiService) : MainRepository {
    override suspend fun getAllPosts() = flow {
        emit(DataStatus.loading())
        val result = apiService.getAllPosts()
        when (result.code()) {
            200 -> emit(DataStatus.success(result.body()?.data?.data))
            400 -> emit(DataStatus.error(result.message()))
            500 -> emit(DataStatus.error(result.message()))
        }
    }
        .catch { emit(DataStatus.error(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    override suspend fun getMyPost(username: String) = flow {
        emit(DataStatus.loading())
        val result = apiService.getMyPost(username)
        when (result.code()) {
            200 -> emit(DataStatus.success(result.body()))
            400 -> emit(DataStatus.error(result.message()))
            500 -> emit(DataStatus.error(result.message()))
        }
    }
        .catch { emit(DataStatus.error(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    override suspend fun updateProfile(request: UpdateProfileRequest) = flow {
        emit(DataStatus.loading()) // Loading state

        val response = apiService.updateProfile(
            request.userId,
            request.name,
            request.avatar,
            request.gender,
            request.address,
            request.introduce
        )
        Log.d("MainRepositoryImpl", "Response: ${response.body()}")
        if (response.isSuccessful) {
            response.body()?.let {
                emit(DataStatus.success(it)) // Thành công
            } ?: emit(DataStatus.error("Empty response body"))
        } else {
            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
            emit(DataStatus.error("Error ${response.code()}: $errorMessage"))
        }
    }.catch { e ->
        emit(DataStatus.error(e.message ?: "Unexpected error"))
    }.flowOn(Dispatchers.IO)

    override suspend fun changePass(request: ChangePassRequest) = flow {
        emit(DataStatus.loading())
        val result = apiService.changePass(
            request.userId,
            request.old_password,
            request.new_password
        )
        if (result.isSuccessful) {
            result.body()?.let {
                emit(DataStatus.success(it)) // Thành công
            } ?: emit(DataStatus.error("Empty response body"))
        } else {
            val errorMessage = result.errorBody()?.string() ?: "Unknown error"
            emit(DataStatus.error("Error ${result.code()}: $errorMessage"))
        }
    }.catch { e ->
        emit(DataStatus.error(e.message ?: "Unexpected error"))
    }.flowOn(Dispatchers.IO)

    override suspend fun addPost(request: PostRequest) = flow {
        emit(DataStatus.loading())
        val result = apiService.addPost(
            request.userId,
            request.images,
            request.content
        )
        if (result.isSuccessful) {
            result.body()?.let {
                emit(DataStatus.success(it)) // Thành công
            } ?: emit(DataStatus.error("Empty response body"))
        } else {
            val errorMessage = result.errorBody()?.string() ?: "Unknown error"
            emit(DataStatus.error("Error ${result.code()}: $errorMessage"))
        }
    }.catch { e ->
        emit(DataStatus.error(e.message ?: "Unexpected error"))
    }.flowOn(Dispatchers.IO)

    override suspend fun deletePost(request: PostDeleteRequest) = flow {
        emit(DataStatus.loading())
        val result = apiService.deletePost(
            request
        )
        if (result.isSuccessful) {
            result.body()?.let {
                emit(DataStatus.success(it)) // Thành công
            } ?: emit(DataStatus.error("Empty response body"))
        } else {
            val errorMessage = result.errorBody()?.string() ?: "Unknown error"
            emit(DataStatus.error("Error ${result.code()}: $errorMessage"))
        }
    }.catch { e ->
        emit(DataStatus.error(e.message ?: "Unexpected error"))
    }.flowOn(Dispatchers.IO)

    override suspend fun likePost(request: LikePostRequest) = flow {
        emit(DataStatus.loading())
        val result = apiService.likePost(
            request
        )
        if (result.isSuccessful) {
            result.body()?.let {
                emit(DataStatus.success(it)) // Thành công
            } ?: emit(DataStatus.error("Empty response body"))
        } else {
            val errorMessage = result.errorBody()?.string() ?: "Unknown error"
            emit(DataStatus.error("Error ${result.code()}: $errorMessage"))
        }
    }.catch { e ->
        emit(DataStatus.error(e.message ?: "Unexpected error"))
    }.flowOn(Dispatchers.IO)


}
