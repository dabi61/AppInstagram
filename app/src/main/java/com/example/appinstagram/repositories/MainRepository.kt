package com.example.appinstagram.repositories

import com.example.appinstagram.model.ChangePassRequest
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.model.PageResponse
import com.example.appinstagram.model.PostDeleteResponse
import com.example.appinstagram.model.PostRequest
import com.example.appinstagram.model.PostResponse
import com.example.appinstagram.model.ProfileResponse
import com.example.appinstagram.model.UpdateProfileRequest
import com.example.appinstagram.utils.DataStatus
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getAllPosts() : Flow<DataStatus<List<HomeData.Post>>>
    suspend fun getMyPost(username : String) : Flow<DataStatus<PageResponse>>
    suspend fun updateProfile(request: UpdateProfileRequest) : Flow<DataStatus<ProfileResponse>>
    suspend fun changePass(request: ChangePassRequest) : Flow<DataStatus<ProfileResponse>>
    suspend fun addPost(request: PostRequest) : Flow<DataStatus<PostResponse>>
    suspend fun deletePost(userId: String, postId: String) : Flow<DataStatus<PostDeleteResponse>>
}