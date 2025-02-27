package com.example.appinstagram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appinstagram.model.ChangePassRequest
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.model.LoginRequest
import com.example.appinstagram.model.Page
import com.example.appinstagram.model.PageResponse
import com.example.appinstagram.model.PostDeleteResponse
import com.example.appinstagram.model.PostRequest
import com.example.appinstagram.model.PostResponse
import com.example.appinstagram.model.Profile
import com.example.appinstagram.model.ProfileResponse
import com.example.appinstagram.model.UpdateProfileRequest
import com.example.appinstagram.model.User
import com.example.appinstagram.repositories.MainRepository
import com.example.appinstagram.repositories.MainRepositoryImpl
import com.example.appinstagram.utils.DataStatus
import kotlinx.coroutines.launch

class  MainViewModel(private val repository: MainRepository) : ViewModel() {
    private val _posts = MutableLiveData<DataStatus<List<HomeData.Post>>>()
    private val _myPost = MutableLiveData<DataStatus<PageResponse>>()
    private val _profile = MutableLiveData<DataStatus<ProfileResponse>>()
    private val _pass = MutableLiveData<DataStatus<ProfileResponse>>()
    private val _deletePost = MutableLiveData<DataStatus<PostDeleteResponse>>()
    private val _addPost = MutableLiveData<DataStatus<PostResponse>>()

    val profile: LiveData<DataStatus<ProfileResponse>>
        get() = _profile

    val pass: LiveData<DataStatus<ProfileResponse>>
        get() = _pass

    val myPost: LiveData<DataStatus<PageResponse>>
        get() = _myPost

    val posts: LiveData<DataStatus<List<HomeData.Post>>>
        get() = _posts

    val addPost: LiveData<DataStatus<PostResponse>>
        get() = _addPost

    val deletePost: LiveData<DataStatus<PostDeleteResponse>>
        get() = _deletePost

    fun getAllPosts() = viewModelScope.launch {
            repository.getAllPosts().collect { dataStatus ->
                _posts.value = dataStatus
            }
    }

    fun getMyPost(username : String) = viewModelScope.launch {
        repository.getMyPost(username).collect{ dataStatus ->
            _myPost.value = dataStatus
        }
    }

    fun updateProfile(request: UpdateProfileRequest) = viewModelScope.launch {
        repository.updateProfile(request).collect{ dataStatus ->
            _profile.value = dataStatus
        }
    }

    fun changePass(request: ChangePassRequest) = viewModelScope.launch {
        repository.changePass(request).collect{ dataStatus ->
            _pass.value = dataStatus
        }
    }
    fun addPost(request: PostRequest) = viewModelScope.launch {
        repository.addPost(request).collect{ dataStatus ->
            _addPost.value = dataStatus
        }
    }
    fun deletePost(userId: String, postId: String) = viewModelScope.launch {
        repository.deletePost(userId, postId).collect{ dataStatus ->
            _deletePost.value = dataStatus
        }
    }
}

