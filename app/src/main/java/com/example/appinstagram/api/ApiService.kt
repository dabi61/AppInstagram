package com.example.appinstagram.api

import androidx.room.Update
import com.example.appinstagram.model.LikePostRequest
import com.example.appinstagram.model.LoginRequest
import com.example.appinstagram.model.LoginResponse
import com.example.appinstagram.model.PageResponse
import com.example.appinstagram.model.PostDeleteRequest
import com.example.appinstagram.model.PostDeleteResponse
import com.example.appinstagram.model.PostResponse
import com.example.appinstagram.model.ProfileResponse
import com.example.appinstagram.model.SignUpRequest
import com.example.appinstagram.model.SignUpResponse
import com.example.appinstagram.model.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("/api/v1/list-post?sort=moi-nhat&page=1&perPage=50")
    suspend fun getAllPosts() : Response<PageResponse>

    @GET("/api/v1/user/hihihi")
    suspend fun getUser() : Response<UserResponse>


    @GET("https://insta.hoibai.net/api/v1/list-post/{username}")
    suspend fun getMyPost(@Path("username") username: String) : Response<PageResponse>

    @POST("/api/v1/login")
    suspend fun login(
        @Body request: LoginRequest
    ) : Response<LoginResponse>

    @POST("/api/v1/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ) : Response<SignUpResponse>

    @Multipart
    @PATCH("/api/v1/user")
    suspend fun updateProfile(
        @Part("userId") userId: RequestBody,
        @Part("name") name: RequestBody?,
        @Part avatar: MultipartBody.Part?,  // Ảnh đại diện
        @Part("gender") gender: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("introduce") introduce: RequestBody?
    ) : Response<ProfileResponse>

    @Multipart
    @PATCH("/api/v1/user")
    suspend fun changePass(
        @Part("userId") userId: RequestBody,
        @Part("old_password") oldPassword: RequestBody?,
        @Part("new_password") newPassword: RequestBody?,
    ) : Response<ProfileResponse>

    @Multipart
    @POST("/api/v1/post")
    suspend fun addPost(
        @Part("userId") userId: RequestBody,
        @Part images: List<MultipartBody.Part?>,
        @Part("content") content: RequestBody?,
    ) : Response<PostResponse>

    @HTTP(method = "DELETE", path = "api/v1/post", hasBody = true)
    suspend fun deletePost(@Body request: PostDeleteRequest): Response<PostDeleteResponse>

    @POST("/api/v1/like")
    suspend fun likePost(
        @Body request: LikePostRequest
    ) : Response<PostDeleteResponse>

//        @Query("sort") sort: String,
//        @Query("page") page: Int,
//        @Query("perPage") perPage: Int
}