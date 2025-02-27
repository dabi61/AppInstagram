package com.example.appinstagram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinstagram.model.LoginRequest
import com.example.appinstagram.model.Profile
import com.example.appinstagram.model.ProfileResponse
import com.example.appinstagram.model.SignUpRequest
import com.example.appinstagram.model.SignUpResponse
import com.example.appinstagram.repositories.LoginRepositoryImpl
import com.example.appinstagram.utils.DataStatus
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: LoginRepositoryImpl) : ViewModel() {
    private val _profile = MutableLiveData<DataStatus<Profile>>()
    val profile : LiveData<DataStatus<Profile>>
        get() = _profile

    private val _newProfile = MutableLiveData<DataStatus<SignUpResponse>>()
    val newProfile : LiveData<DataStatus<SignUpResponse>>
        get() = _newProfile

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            repository.login(request).collect { dataStatus ->
                _profile.postValue(dataStatus) // Cập nhật LiveData
            }
        }
    }

    fun signUp(request: SignUpRequest) {
        viewModelScope.launch {
            repository.signUp(request).collect { dataStatus ->
                _newProfile.postValue(dataStatus) // Cập nhật LiveData
            }
        }
    }
}